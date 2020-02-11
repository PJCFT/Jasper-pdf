# Jasper-pdf
JasperReport 报表工具实现pdf打印
# I.实现生命周期及简单原理

生命周期：
设计（Design）阶段、执行（Execution）阶段以及输出（Export）阶段

简单原理：
JRXML:报表填充模板，本质是一个XML。
Jasper:由JRXML模板编译生成的二进制文件，用于代码填充数据。
Jrprint:当用数据填充完Jasper后生成的文件，用于输出报表。
Exporter:决定要输出的报表为何种格式，报表输出的管理类。

# II.实现过程
## jasper 模板文件的生成
需要安装Jasper Studio 新建jrxml文件，然后在Studio软件上画出符合实际需求的模板文件，进行编译，编译后的文件就是japser二进制文件
把文件放到项目的resources下的templates文件夹下

## 1.添加依赖

```
<dependencies>
        <!-- spring boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- thymeleaf-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!--jasper begin-->
        <dependency>
            <groupId>net.sf.jasperreports</groupId>
            <artifactId>jasperreports</artifactId>
            <version>6.5.0</version>
        </dependency>
        <dependency>
            <groupId>org.olap4j</groupId>
            <artifactId>olap4j</artifactId>
            <version>1.2.0</version>
        </dependency>
        <dependency>
            <groupId>com.lowagie</groupId>
            <artifactId>itext</artifactId>
            <version>2.1.7</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>4.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>4.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml-schemas</artifactId>
            <version>4.0.1</version>
        </dependency>
        <!--jasper end -->
    </dependencies>
```

## 2.JasperController实现

```
@RequestMapping(value = "/pdf",method = RequestMethod.GET)
    public void downPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.加载jasper文件
        Resource resource = new ClassPathResource("templates/swryyjd.jasper");
        //获取文件输入流
        FileInputStream fis = new FileInputStream(resource.getFile());
        //文件输出流
        ServletOutputStream os = response.getOutputStream();
        //2.创建JasperPrint，向模板填充数据
        try {
            //此处根据实际情况将需要填充到模板的数据put进去即可
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("tbsj","2020年02月11日");
            parameter.put("ajmc","XXX名称");
            parameter.put("nsrsbh","111111111111111111");
            parameter.put("fs","2");
            parameter.put("yjr","某某男");
            parameter.put("qsr","某某女");
            parameter.put("yjrq","2020年02月11日");
            parameter.put("qsrq","2020年02月11日");
            parameter.put("ldyj","同意！");
            parameter.put("zlmu","资料目录111111");
            parameter.put("fzryj","同意！！！");
            parameter.put("fzryjrq","2020年02月11日");
            parameter.put("ldyjrq","2020年02月11日");

            //空的数据源
            JRDataSource dataSource = new JREmptyDataSource();

            /**
             * fis：jasper文件输入流
             * parameter：向模板传入的参数
             * dataSource：数据源（和数据库数据源不同）
             * 具体指的是填充模板的数据源（connection, javaBean, Map)
             * 填充空的数据源（new JREEmptyDataSource()）
             * ps:第三个参数如果是空数据源的话，需要填上去，这个参数不能缺失，jasper report的bug
             *JasperFillManager的工具类中的
             * fillReport(InputStream inputStream, Map<String, Object> parameters, JRDataSource dataSource)
             */
            JasperPrint print = JasperFillManager.fillReport(fis,parameter,dataSource);
            //3.将JasperPrint已pdf输出流形式输出 JasperExportManager工具类中的
            //exportReportToPdfStream(JasperPrint jasperPrint, OutputStream outputStream)
            JasperExportManager.exportReportToPdfStream(print,os);
        } catch (JRException e) {
            e.printStackTrace();
        }finally {
            //数据流的刷新
            os.flush();
        }
    }
```

## 3.中文乱码处理
### 3.1 新增处理中文乱码配置文件jasperreports_extension.properties
```
net.sf.jasperreports.extension.registry.factory.simple.font.families=net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory
net.sf.jasperreports.extension.simple.font.families.lobstertwo=stsong/fonts.xml
```
### 3.2 拷贝对应的字体文件到resources目录下

对应字体资源在项目中resources下的stsong文件夹所有文件

## 4.最终效果
![image](https://github.com/PJCFT/Jasper-pdf/blob/master/%E6%95%88%E6%9E%9C%E5%9B%BE.jpg)

