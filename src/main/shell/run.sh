#!/usr/bin/env bash
env SWT_GTK3=0 SWT_WEBKIT2=1
echo $SWT_GTK3
java \
-Dorg.eclipse.swt.browser.DefaultType=webkit \
-Dorg.eclipse.swt.browser.UseWebKitGTK=true \
-Djava.ext.dirs=libs \
org.pentaho.di.ui.spoon.Spoon
