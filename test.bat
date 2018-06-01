@echo off

set WEBCAM_ID=%1
set WEBCAM_IP=%2
set STORAGE_PATH=%3
set RECORD_TIME=%4

rem set SAVESTAMP=%DATE:/=-%@%TIME::=-%
rem set FILE_NAME=%WEBCAM_ID%_%SAVESTAMP:,=.%.mp4
rem set STORAGE_PATH=%STORAGE_FOLDER%%FILE_NAME%

set STREAM_PATH="http://%WEBCAM_IP%/control/faststream.jpg?stream=MxPEG&framestep=1&fps=0.00&jpheadrefresh=3&extmxm"

echo %STREAM_PATH%
echo %STORAGE_PATH%
echo %RECORD_TIME%

rem "C:\Program Files\VideoLAN\VLC\vlc.exe" -I dummy --run-time=%RECORD_TIME% --live-caching 2000 --avformat-format=mxg %STREAM_PATH% --sout="#transcode{vcodec=h264,acodec=none}:duplicate{dst=std{access=file,mux=mp4, dst='%STORAGE_PATH%'},dst=nodisplay}" vlc://quit