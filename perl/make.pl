#!/usr/bin/perl -I/home/phil/z/Geometry/perl/
#-------------------------------------------------------------------------------
# Compile, install and run a generic Android app
# 𝗣𝗵𝗶𝗹𝗶𝗽 𝗥 𝗕𝗿𝗲𝗻𝗮𝗻  at gmail dot com, Appa Apps Ltd Inc 2015/08/14 17:12:39
# I, the author of this work, hereby place this work in the public domain.
#-------------------------------------------------------------------------------

use v5.18;
use warnings FATAL => qw(all);
use strict;
use Data::Dump qw(dump);
use util;

#-------------------------------------------------------------------------------
# Actions - set action to your desired action or run from command line as below
#-------------------------------------------------------------------------------

my @action = qw(run);
# install run

#-------------------------------------------------------------------------------
# Constants - edit to reflect your directory structure or set in config.pl
#-------------------------------------------------------------------------------

my $home        = qq(/home/phil/z/Geometry/);                                   # Home directory
my $name        = qq(Activity);                                                 # Name of the app
my $title       = qq(Activity);                                                 # Title of the piece
my $icon        = qw(/home/phil/vocabulary/supportingDocumentation/images/Jets/EEL.jpg); # Icon for app - choose any picture on your system and an icon will be created from it
my $package     = qq(com.appaapps.generic);
my $sdk         = qq(/home/phil/Android/sdk/);                                  # Android SDK on your local machine
my $sdkLevels   = [15,23];                                                      # Min sdk, target sdk
my $version     = '1';                                                          # Version number
my $device      = qq(3024600145324307);                                         # Install on Nook
#  $device      = qq(94f4d441);                                                 # Install on Nokia
#  $device      = qq(emulator-5554);                                            # Install on emulator
my @permissions =                                                               # Permissions for app
  qw(INTERNET ACCESS_WIFI_STATE ACCESS_NETWORK_STATE WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE RECEIVE_BOOT_COMPLETED);

my $Src         = $home.'src/';                                                 # Edit source code here
my @lib         = qw(/home/phil/vocabulary/supportingDocumentation/libs/Colours.jar); # Add libraries here
my $perl        = $home.'perl/';                                                # Perl
my $tmp         = $home.'tmp/';                                                 # App layout directories
my $app         = $tmp.'app/';
my $ant         = $app."ant.properties";                                        # Ant properties file
my $src         = $app.'src/';
my $lib         = $app.'libs/';
my $res         = $app.'res/';
my $apk         = $app.'bin/'.$name.'-debug.apk';                               # Resulting apk
my $man         = $app.'AndroidManifest.xml';                                   # Manifest file

#-------------------------------------------------------------------------------
# Create icons for app
#-------------------------------------------------------------------------------

sub pushIcon                                                                    # Create and transfer each icon
 {my ($size, $dir) = @_;
  for my $i(qw(ic_launcher))
   {for my $d(qw(drawable))
     {my $t = $tmp.'icon.png';
      makePath($t);
      my $c = "convert -strip $icon -resize ${size}\\\!x${size}\\\! $t";
      qx($c);

      my $T = $res.$d.'-'.$dir.'dpi/'.$i.'.png';
      copyFile($t, $T);
     }
   }
 }

sub pushIcons
 {pushIcon(@$_) for ([48, "m"], [72, "h"], [96, "xh"], [144, "xxh"]);           # Create icons
 }

#-------------------------------------------------------------------------------
# Create manifest for app
#-------------------------------------------------------------------------------

sub permissions                                                                 # Create permissions
 {my $P = "android.permission";
  my %p = (map {$_=>1} @permissions);
  my $p = "\n";

  for(sort keys %p)
   {$p .= "  <uses-permission android:name=\"$P.$_\"/>\n";
   }

  $p
 }

sub manifest
 {my $permissions = permissions;
  my ($minSdk, $targetSdk) = @$sdkLevels;

  my $manifest = << "END";
<?xml version="1.0" encoding="utf-8"?>
  <manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="$package"
    android:installLocation="auto"
    android:versionCode="$version"
    android:versionName="\@string/versionName" >

  <uses-sdk
    android:minSdkVersion="$minSdk"
    android:targetSdkVersion="$targetSdk"/>
  <application
    android:allowBackup="true"
    android:icon="\@drawable/ic_launcher"
    android:largeHeap="true"
    android:debuggable="true"
    android:hardwareAccelerated="true"
    android:label="\@string/app_name">
    <activity
      android:name=".$name"
      android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
      android:screenOrientation="sensor"
      android:theme="\@android:style/Theme.NoTitleBar"
      android:label="\@string/app_name">
      <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
        <category android:name="android.intent.category.LAUNCHER"/>
      </intent-filter>
    </activity>
  </application>
  $permissions
</manifest>
END
  writeFile($man, $manifest);
 }

#-------------------------------------------------------------------------------
# Create resources for app
#-------------------------------------------------------------------------------

sub resources()
 {my $t = << "END";
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">$title</string>
    <string name="versionName">$version</string>
</resources>
END
  writeFile($res."values/strings.xml", $t);
 }

#-------------------------------------------------------------------------------
# Copy source - could be improved by adding package name automatically
#-------------------------------------------------------------------------------

sub copySource
 {my $s = $Src;
  my $p = $package =~ s/\./\//gr;
  my $t = $src.$p.'/';

  qx(rsync -r $s $t);
 }

#-------------------------------------------------------------------------------
# Copy libraries
#-------------------------------------------------------------------------------

sub copyLibs {qx(rsync -r $_ $lib) for @lib}

#-------------------------------------------------------------------------------
# Ant properties
#-------------------------------------------------------------------------------

sub antProperties
 {my $s = <<'END';
renderscript.support.mode=true
java.source=1.5
END
	writeFile($ant, $s);
 }

#-------------------------------------------------------------------------------
# Create app
#-------------------------------------------------------------------------------

sub create
 {qx(rm -r $app);
#                                                        android list targets
  my $cmd = "$sdk./tools/android create project --target 1 --name $name --path $app --activity $name --package $package";
  qx($cmd);                                                                     # Need check of result
  pushIcons;                                                                    # Create icons
  copySource;                                                                   # Copy source
  copyLibs;                                                                     # Copy libraries
  antProperties;                                                                # Ant properties
  manifest;                                                                     # Create manifest
  resources;                                                                    # Create resources
 }

#-------------------------------------------------------------------------------
# Make app
#-------------------------------------------------------------------------------

sub make                                                                        # Make locally using some of the artefacts built by make mms
 {my $cmd = "ant -f $app./build.xml debug";                                     # Command
  my $r = execCmd($cmd);                                                        # Perform compile

  Confess $r if $r !~ m/BUILD SUCCESSFUL/;
 }

#-------------------------------------------------------------------------------
# Install app
#-------------------------------------------------------------------------------

sub install                                                                     # Install on emulator
 {my $adb = $sdk."platform-tools/adb";
  execCmd("$adb -s $device install -r $apk");
  execCmd("$adb -s $device shell am start $package/.$name");
 }

#-------------------------------------------------------------------------------
# Actions
#-------------------------------------------------------------------------------

sub cInstall                                                                    # Install on emulator
 {Advise "Install";
  install;
 }

sub cRun                                                                        # Create, make, install
 {Advise "Run";
  create;
  make;                                                                         # Command
  install;                                                                      # Perform compile
 }

#-------------------------------------------------------------------------------
# Perform actions
#-------------------------------------------------------------------------------

my @o = (@ARGV ? @ARGV : @action);

while(@o)
 {local $_ = shift @o;

  if    (/\Arun\z/i)            {cRun}                                          # Run app
  elsif (/\Ainstall\z/i)        {cInstall}                                      # Install on emulator
  else
   {Carp "Ignored unknown command: $_";
   }
 }

normalFinish;
