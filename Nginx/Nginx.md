## 一、Nginx的基础配置

```nginx
#user  nobody;	#配置运行Nginx的用户组，nobody代表都可以运行
worker_processes  auto;		# 配置工作进程数

#error_log  logs/error.log;		# 配置错误日志存放位置
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;		# 配置进程号存放位置


events {
    worker_connections  1024;	# 设置运行每一个进程同时开启的最大连接数
    accept_mutex on;	# 设置网络连接序列化，为解决惊群问题，默认为on；惊群：当只有一个连接到来时，多个进程被唤醒，影响性能
    multi_accept off;	# 设置是否运行同时接受多个网络连接，默认为off
    use select;		# 选择事件驱动模型
}


http {
    include       mime.types;	# 引入mime.types配置文件
    default_type  application/octet-stream;	# 配置用于处理前端请求的MIME类型

    # 日志格式
    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';
	
    # 连接日志 参数：路径、格式、缓冲区大小
    #access_log  logs/access.log  main;

    sendfile        on;		# 配置允许sendfile方式传输文件 默认值为off
    sendfile_max_chunk 0;	# 每次调用sendfile()传输的最大数据量，0代表无限制
    #tcp_nopush     on;	

    #keepalive_timeout  0;	# 连接超时时间
    # 该配置含义是：服务端保持连接的时间设置为65s，发给用户端的应答报文头部中Keep-Alive域的超时时间设置为50s
    keepalive_timeout  65s 50s;
    keepalive_requests 100;	# 设置单连接请求数上限

    #gzip  on;

    server {
        listen       80;	# 监听80端口
        listen       192.168.1.10;	# 监听具体IP的所有端口连接
        #server_name  ~^www\d+\.myserver\.com$;	基于名称的虚拟主机配置
        #server_name  192.168.1.31; # 基于IP的虚拟主机配置
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;	# 请求的根目录
            index  index.html index.htm;	# 设置网站默认首页
            # 不允许同时设置多个IP，需要重复使用allow指令
            allow  192.168.1.31;	# 配置Nginx的访问权限 all代表允许所有客户端访问
            deny 192.168.1.23; 		# 禁止访问的客户端IP all代表禁止所有客户端访问
            auth_basic 请输入密码; # 配置基于密码的访问权限 off代表关闭
            auth_basic_user_file pass_file; # 设置包含用户名和密码的文件路径
        }
        
        location ~ ^/data/(.+\.(htm|htm)) $ {
            # 收到/data/index.htm请求时，在/locationtest/other目录下寻找index.htm
            alias  /locationtest/other/$1; # 更改URI
        }
        
        location ~ ^/data/(.+)/web/ $ {
            # 收到/data/locationtest/web/请求时，在/data/locationtest/web/目录下依次寻找index.locationtest.html index.my1.htm index.html
           	index  index.$1.html index.my1.htm index.html;
        }

        #error_page  404              /404.html;	

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;	# 设置网页的错误页面
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }
}

    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}

```





## 二、Nginx服务器的Gzip压缩

```nginx
server {
  	listen 80;
  	server_name location;
    
  	gzip on;	# 开启Gzip功能 默认为off
  	gzip_buffers 16 8k;	# 设置Gzip压缩文件使用缓存空间的大小 参数：缓存个数，缓存大小 默认情况下number*size的值为128,
  	gzip_comp_level 6;	# 设置压缩级别，级别越高，效率越低，压缩程度越大
    gzip_disable MSIE [4-6]\.; # 设置不使用Gzip功能的客户端类型，支持正则表达式
    gzip_http_version 1.1; # 设置使用Gzip功能的最低版本HTTP协议 解决低版本不支持Gzip的问题
    gzip_min_length 1024;	# 设置响应页面启用Gzip的最小字节数 0代表全部压缩 解决压缩很小的数据时，可能出现越压越大的情况
  	gzip_vary on;	# 设置是否在响应头部中添加“Vary: Accept-Encoding”以告诉接受方数据经过了压缩
  	gzip_types text/plain; 	# 设置使用Gzip功能的页面类型
    gunzip_static on; # 如果客户端不支持Gzip处理，Nginx服务器就返回解压后的数据，如果支持，仍然返回压缩数据
    gunzip_buffers 32 4K; # 与gzip_buffers相同，申请解压文件使用缓存空间大小
}
```





## 三、Nginx服务器的Rewrite功能