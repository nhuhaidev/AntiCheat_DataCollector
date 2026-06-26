@echo off
echo ==========================================
echo            KHOI DONG HE THONG
echo ==========================================

:: 1. Khởi động luồng Python ETL
start "Python ETL Pipeline" cmd /k "python -u D:\DoAnNganh\DataPipeline_Python\etl_pipeline.py"

:: Đợi 3 giây cho Python ổn định
timeout /t 3 /nobreak > NUL

:: 2. Khởi động Server Minecraft
cd /d "D:\DoAnNganh\PaperServer"
java -Xms2G -Xmx2G -jar server.jar nogui

pause