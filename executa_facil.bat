@echo off
echo Iniciando UFS Bank...

:: Abre o Servidor em uma nova janela
start "Servidor UFS Bank" cmd /k "java -jar UFS_BANK-0.0.1-SNAPSHOT-server.jar"

:: Espera 3 segundos para o servidor subir
timeout /t 3 /nobreak > nul

:: Abre o Cliente em outra janela
start "Cliente UFS Bank" cmd /k "java -jar UFS_BANK-0.0.1-SNAPSHOT-client.jar"

echo Servidor e Cliente estao rodando!
pause