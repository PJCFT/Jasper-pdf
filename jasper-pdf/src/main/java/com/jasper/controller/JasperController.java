package com.jasper.controller;/*
 *@author: PJC
 *@time: 2020/2/10
 *@description: null
 */

import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/jasper")
public class JasperController {

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
}
