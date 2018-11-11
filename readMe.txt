JavaMail邮箱配置文件：mailConfig.properties
QQ邮箱
mail.smtp.host=smtp.qq.com
第一，QQ邮箱必须开启smtp
第二，然后获取下授权码（发短信，然后页面会显示授权码，一定要复制另存，不要问我怎么知道的，我发了n次短信！）
mail.sender.username=
授权码
mail.sender.password=
QQ开启ssl 端口995

sunreal公司邮箱
mail.smtp.host=
mail.sender.username=
mail.sender.password=

因为我开启了TLS加密，所以需要去foxmail 够选中STARTTLS加密传输
或者mail.smtp.starttls.enable=false

未开启ssl 端口25
开启ssl 465（587）
点击链接加入群聊【Java开源技术分享】：https://jq.qq.com/?_wv=1027&k=5qOUZIX<br>
551404618Java开源技术分享