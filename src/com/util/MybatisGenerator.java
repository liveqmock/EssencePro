/*
 * Copyright (c) 2013 zljysoft. 中联佳裕信息技术（北京）有限公司 版权所有 
 * http://www.zljysoft.com
 * File Name：MybatisGenerator.java
 * Comments: mybatis 代码生成工具
 * Author: 
 * Create Date: 2013-4-8 上午11:13:17
 * Modified By: 
 * Modified Date: 
 * Why & What is modified: 
 * version: V1.0 
 */
package com.util;

import org.mybatis.generator.ant.GeneratorAntTask;

/**
 * mybatis 代码生成工具 生成实体、mapper接口、sql配置文件
 * 
 * @author 玄承勇
 * @date 2013-4-10 上午11:06:53
 * @version V1.0
 * 
 */
public class MybatisGenerator {

	private static String configPath = "src/resources/mybatis/generatorConfig.xml";

	public static void main(String[] args) {
		try {
			GeneratorAntTask task = new GeneratorAntTask();
			task.setConfigfile(configPath);
			task.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
