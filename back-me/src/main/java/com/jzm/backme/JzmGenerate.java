package com.jzm.backme;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class JzmGenerate
{

    public static void main(String[] args)
    {
        // 这个字符串,从table
        String tabStr = "user_role";

        String prefix = ""; // 生成要拼接的表前缀，如tb_str、ts_str、tb_1字符串,设置tb_,只会拼接tb_的
        String filterPrefix = "";// 过滤表前缀，生成的实体类把前缀忽略
        // 格式化之后table字符串
        String tableStr = JzmGenerate.tabStrFormat(tabStr, prefix);

        JzmGenerate.generateAll(tableStr, filterPrefix);
    }

    /**
     * @param tableStr     表格字符串: 如 " sys_user,sys_role "
     * @param filterPrefix 过滤表格前缀： 如 sys_
     */
    public static void generateAll(final String tableStr, final String filterPrefix)
    {
        // 设置url。用户名和密码
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/nice?characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
                        "root", "123456")
                .globalConfig(builder ->
                {
                    builder.author("qhx2004") // 设置作者
                            .enableSwagger() // 开启 swagger 模式,选的话+依赖
                            .dateType(DateType.TIME_PACK)
                            .outputDir("C:\\Users\\qhx20\\Desktop\\区块链比赛资料整理\\2023国赛样题\\国9\\代码\\back-me\\src\\main\\java");
                    // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) ->
                {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT)
                    {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder ->
                {
                    builder.parent("com.jzm") // 设置父包名
                            .entity("domain")
                            .moduleName("backme") // 设置父包模块名,我这里没有模块名
                            // C:\Users\qhx20\Desktop\wms\wms-h\src\main\resources
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Users\\qhx20\\Desktop\\区块链比赛资料整理" +
                                    "\\2023国赛样题\\国9\\代码\\back-me\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder ->
                {
                    builder.addInclude(tableStr) // 设置需要生成的表名
                            .addTablePrefix(filterPrefix)// 设置过滤表前缀
                            // 设置controller
                            .controllerBuilder().enableRestStyle()  // 启用rest风格
                            // 设置entity实体
                            .entityBuilder().enableLombok() // 开lomback
                            .enableFileOverride() // 覆盖已有文件
                            // 设置service
                            .serviceBuilder().formatServiceFileName("%sService")  // 格式化生成Service.java文件，相当于把I过滤了
                            // 设置 mapper
                            .mapperBuilder().enableMapperAnnotation(); // 开启mapper注解

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }


    /**
     * @param tabStr 表格字符串 : 就是直接从 从客户端软件 show tables; 直接复制粘贴的。
     * @param prefix 要格式化的表格字符串前缀,最后会组合到一起。( 如 sys_user、sys_role )
     * @return
     */
    public static String tabStrFormat(String tabStr, String prefix)
    {
        StringBuilder stb = new StringBuilder();
        String[] splits = tabStr.split("\n");
        for (String split : splits)
        {
            if (split.startsWith(prefix))
            {
                stb.append(split);
                stb.append(",");
            }

            if (prefix == null)
            {
                stb.append(split);
                stb.append(",");
            }
        }
        return stb.toString().substring(0, stb.toString().length() - 1);
    }

}