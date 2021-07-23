###########################################
cd /opt/taotao-bigdata

wget https://mirrors.bfsu.edu.cn/apache/hadoop/common/hadoop-3.3.0/hadoop-3.3.0.tar.gz

tar -zxvf hadoop-3.3.0.tar.gz

cd hadoop-3.3.0/etc/hadoop

yum install openssl-devel

hadoop checknative

# 1.添加环境变量
# 2.修改配置文件

###########################################
hdfs namenode -format

ssh-keygen -t rsa
touch ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
cat ~/.ssh/id_rsa.pub  >> /root/.ssh/authorized_keys

yarn-site.xml
<!-- 开启日志聚集功能 -->
	<property>
		<name>yarn.log-aggregation-enable</name>
		<value>true</value>
		<description>mapred --daemon start historyserver 启动</description>
	</property>
	<!-- 设置日志聚集服务器地址 -->
	<property>
		<name>yarn.log.server.url</name>
		<value>http://172.16.3.240:19888/jobhistory/logs</value>
	</property>
	<!-- 设置日志保留时间为 7 天 -->
	<property>
		<name>yarn.log-aggregation.retain-seconds</name>
		<value>604800</value>
	</property>

start-dfs.sh

start-yarn.sh

hdfs --daemon start httpfs

#开启历史服务器
mapred --daemon start historyserver

查看 JobHistory
http://172.16.3.240:19888/jobhistory

hadoop fs -mkdir -p /tmp
hadoop fs -chmod 777 /tmp
hadoop fs -mkdir -p /user/hive/warehouse
hadoop fs -chmod 777 /user/hive/warehouse
hadoop fs -mkdir -p /user/hue
hadoop fs -chmod 777 /user/hue

wget http://www.congiu.net/hive-json-serde/1.3.8/hdp23/json-serde-1.3.8-jar-with-dependencies.jar
wget http://www.congiu.net/hive-json-serde/1.3.8/hdp23/json-udf-1.3.8-jar-with-dependencies.jar

hadoop fs -mkdir -p /common/lib
hadoop fs -chmod 777 /common/lib
hdfs dfs -put -f /opt/soft/json-serde-1.3.8-jar-with-dependencies.jar /common/lib
hdfs dfs -put -f /opt/soft/json-udf-1.3.8-jar-with-dependencies.jar /common/lib

###########################################
#!/bin/bash

function start_hadoop() {
    start-dfs.sh
    echo "dfs started"
    sleep 20

    start-yarn.sh
    echo "yarn started"

    start-history-server.sh
}

function stop_hadoop() {
    stop-yarn.sh
    echo "dfs stoped"
    sleep 10

    stop-dfs.sh
    echo "dfs stoped"

    stop-history-server.sh
}

case $1 in
"start")
    start_hadoop
    ;;
"stop")
    stop_hadoop
    ;;
"restart")
    stop_hadoop
    sleep 15
    start_hadoop
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
