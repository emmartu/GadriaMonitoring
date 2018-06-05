@echo off

set WEBCAM_ID=%1
set WEBCAM_IP=%2
set STORAGE_PATH=%3
set RECORD_TIME=%4
set VIDEOLAN_EXE_PATH=%5

set STREAM_PATH="http://%WEBCAM_IP%/control/faststream.jpg?stream=MxPEG&framestep=1&fps=0.00&jpheadrefresh=3&extmxm"

%VIDEOLAN_EXE_PATH% -I dummy --run-time=%RECORD_TIME% --avformat-format=mxg %STREAM_PATH% --sout="#transcode{vcodec=h264,acodec=none}:duplicate{dst=std{access=file,mux=mp4, dst='%STORAGE_PATH%'},dst=nodisplay}" vlc://quit