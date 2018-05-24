@echo off

set SAVESTAMP=%DATE:/=-%@%TIME::=-%
set FILE_NAME=%SAVESTAMP:,=.%.mp4

FOR /F "tokens=3,* delims=.=" %%G IN (config.properties) DO ( set %%G=%%H )

rem now use below vars
if "%%G"=="StorageFolder"
 set StorageFolder=%%H
if "%%G"=="DiskSpace"
 set DiskSpace=%%H
if "%%G"=="MinDiskSpace"
 set MinDiskSpace=%%H
if "%%G"=="VideoLanExePath"
 set VideoLanExePath=%%H

echo %VideoLanExePath%


rem %VideoLanExePath% -I dummy --run-time=10 --live-caching 2000 --avformat-format=mxg "http://111.0.0.1/control/faststream.jpg?stream=MxPEG&framestep=1&fps=0.00&jpheadrefresh=3&extmxm" --sout="#transcode{vcodec=h264,acodec=none}:duplicate{dst=std{access=file,mux=mp4, dst='C:\Users\Lele\Documents\LavoroWebCamMobotix\AVI_Exports\%FILE_NAME%'},dst=nodisplay}" vlc://quit