set DTALK_DEBUG_OPTS=-Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n
call %~dp0\activemq_console.bat %*
