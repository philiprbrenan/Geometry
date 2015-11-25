#!/usr/bin/perl
#-------------------------------------------------------------------------------
# Useful functions
# 𝗣𝗵𝗶𝗹𝗶𝗽 𝗥 𝗕𝗿𝗲𝗻𝗮𝗻  at gmail dot com, Appa Apps Ltd Inc 2015/08/14 17:12:39
#/ I, the author of this work, hereby place this work in the public domain.
#-------------------------------------------------------------------------------

use v5.18;
use warnings FATAL => qw(all);
use strict;
use Carp;
use File::Basename;
use File::Copy;
use File::Find;
use File::Path qw(make_path remove_tree);
use POSIX qw(strftime);                                                         # http://www.cplusplus.com/reference/ctime/strftime/
use Term::ANSIColor;

#-------------------------------------------------------------------------------
# Date/time
#-------------------------------------------------------------------------------

sub dateTimeStamp() {strftime('%Y%m%d_%d_%b_%Y_at_%H_%M_%S', localtime)}
sub timeStamp       {colored(strftime('%H:%M:%S', localtime), 'blue')}

#-------------------------------------------------------------------------------
# Logging
#-------------------------------------------------------------------------------

sub Log(@)            {say STDERR timeStamp, ' ', @_; }
sub Advise(@)         {Log(colored(join('', @_), 'green bold on_yellow'))}
sub Normal(@)         {Log(colored(join('', @_), 'green bold'))}
sub Carp(@)           {Log(colored(join('', @_), 'red'))}
sub Confess(@)        {Log(colored(join('', @_), 'red bold')); use Carp; Carp::confess();}
sub normalFinish(@)                                                             # Confirm action completed and exit
 {my ($package, $filename, $line) = caller();
  Advise("Normal Finish on line $line", (@_ ? ": " : ''), @_);                  # Message
  exit(0);
 }

#-------------------------------------------------------------------------------
# Debug point
#-------------------------------------------------------------------------------

sub step {Log "Step";}

#-------------------------------------------------------------------------------
# Execute a command returning the captured STDOUT+STDERR
#-------------------------------------------------------------------------------

sub execCmd($;$)                                                                # Execute a command
 {my ($cmd, $dump) = @_;                                                        # Command, optional file to write output to
  Normal $cmd unless $dump;                                                     # Show command
  my $r = qx((($cmd) || echo FAILED) 2>&1);                                     # Execute and mark failure if any
  if ($r =~ /FAILED/)                                                           # Test for failure
   {Confess $r                    unless $dump;                                 # Results
    &writeFile($dump, "$cmd\n$r") if $dump;                                     # Save to file
   }
  $r                                                                            # Return results
 }

sub execCmd2($;$)                                                                # Execute a command
 {my ($cmd, $dump) = @_;                                                        # Command, optional file to write output to
  Advise $cmd unless $dump;                                                     # Execute and capture output
  my $r = qx($cmd 2>&1);                                                        # Show results
  Normal $r unless $dump;                                                       # Results
  &writeFile($dump, "$cmd\n$r") if $dump;                                       # Save to file
  $r                                                                            # Return results
 }

#-------------------------------------------------------------------------------
# Execute a set of commands
#-------------------------------------------------------------------------------

sub execCmds($)
 {my ($cmds) = @_;
  for(split /\n/, $cmds)
   {say STDERR $_;
    print STDERR for qx($_);
   }
 }
#-------------------------------------------------------------------------------
# Make a path for a file
#-------------------------------------------------------------------------------

sub makePath($)                                                                 # Make a directory for a file
 {my ($path) = @_;
  if ($path =~ /\A(.+[\\\/])/)
   {$path = $1;
   }

  return 0 if -d $path;
  make_path($path);
  -d $path or Confess "Cannot makePath $path";
  1
 }

#-------------------------------------------------------------------------------
# Read file into string
#-------------------------------------------------------------------------------

sub readFile($)
 {my ($f) = @_;
  $f or Confess "readFile: No file specified";
  open my $F, "<$f" or Confess "Cannot open $f for input";
  local $/ = undef;
  <$F>;
 }

#-------------------------------------------------------------------------------
# Write a string to a file
#-------------------------------------------------------------------------------

sub writeFile($$)
 {my ($f, $s) = @_;                                                             # File, string
  if ($f =~ /\A(.+[\\\/])/)
   {my $d = $1;
    makePath($d);
   }

  Carp "Rewriting file $f" if -e $f;                                            # Complain if we overwrite a file

  open my $F, ">$f" or Confess "writeFile: cannot open $f for write";
  say    {$F} $s;
  close  ($F);
  -e $f or Confess "writeFile: failed to write to file $f";
 }

#-------------------------------------------------------------------------------
# Copy a file
#-------------------------------------------------------------------------------

sub copyFile($$)                                                                # Copy a file
 {my ($source, $target) = @_;
     $source or Confess "No source file specified for copyFile";
  -e $source or Confess "copyFile: cannot copy because source file does not exist, source=$source";

  makePath($target);
  copy($source, $target);                                                       # 0 - if already exists, 1 - if created
  -e $target or Confess "copyFile: copied file does not exist: source=$source, target=$target";
 }

#-------------------------------------------------------------------------------
# Find all the files in a directory tree
#-------------------------------------------------------------------------------

sub getAllFiles
 {my ($path) = @_;                                                              # Path to search
  my $F;
  find(sub
   {return if -d $File::Find::name;                                             # Do not save directories
    $F->{$File::Find::name} = $_;
   }, $path);
  $F
 }

#-------------------------------------------------------------------------------
# Tabularize text
#-------------------------------------------------------------------------------

sub table($;$$)
 {my ($d, $c, $w) = @_;                                                         # Data, control, width
  my @D;
  for   my $e(@$d)
   {for my $D(0..$#$e)
     {my $a = $D[$D] // $w->[$D] // 0;
      my $b = length($e->[$D]) // 0;
      $D[$D] = ($a > $b ? $a : $b);
     }
   }
  my @t;
  for   my $e(@$d)
   {my $t = '';
    for my $D(0..$#$e)
     {if (substr(($c//'').('L'x($D+1)), $D, 1) =~ /L/i)
       {$t .= substr(($e->[$D]//'').(' 'x$D[$D]), 0, $D[$D])."  ";
       }
      else
       {$t .= substr((' 'x$D[$D]).($e->[$D]//''), -$D[$D])."  ";
       }
     }
    push @t, $t;
   }
  @t
 }

#-------------------------------------------------------------------------------
# New line
#-------------------------------------------------------------------------------

sub nl {"\n"}

#-------------------------------------------------------------------------------
# Get the size of a file
#-------------------------------------------------------------------------------

sub fileSize($)
 {my ($f) = @_;
  (stat($f))[7]
 }

#-------------------------------------------------------------------------------
# Size in megabytes
#-------------------------------------------------------------------------------

sub formatDecimal($$)
 {my ($s, $d) = @_;
  my $n = int(100*$s/$d)/100;
  sprintf("%6.2f", $n) =~ s/\A(0|\s)+//r =~ s/(0|\s)+\Z//r =~ s/\.\Z//r =~ s/\A\./0./r;
 }

sub megabytes($)
 {my ($s) = @_;
  return $s unless $s;
  return formatDecimal($s, 1024*1024)." megabytes" if $s > 1024*1024/10;
  return formatDecimal($s, 1024)     ." kilobytes" if $s >      1024/10;
  "$s bytes"
 }

1
