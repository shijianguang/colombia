cmd="scp -P 50001 $PWD/$1 azureuser@xt-test01.chinacloudapp.cn:~/workspace"
echo "Run $cmd"
$cmd
