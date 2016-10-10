cmd="scp -P 50001 $PWD/$1 azureuser@xt-api01.chinacloudapp.cn:~/"
echo "Run $cmd"
$cmd
cmd="scp -P 50002 $PWD/$1 azureuser@xt-api01.chinacloudapp.cn:~/"
echo "Run $cmd"
$cmd
