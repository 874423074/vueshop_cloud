package com.markerhub.core.uitls;


import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 新模块划分后，处理类的包路径问题的工具类
 */
public class FixImportUtil {

	public static void main(String[] args) {

		// 旧项目路径
		String oldPath = "D:\\git-job\\vueshop\\src\\main";

		// 新项目路径
		String newPath = "E:\\git-markerhub\\vueshop-cloud";

		// 获取旧项目中所有文件路径
		Set<String> oldFiles = new HashSet<>();
		listJavaAndXmlFiles(oldFiles, oldPath);

		oldFiles.forEach(System.out::println);

		// 获取旧项目的所有类键值对：AppUser -> com.markerhub.entity.AppUser
		Map<String, String> oldKvs = new HashMap<>();
		oldFiles.forEach(e -> oldKvs.put(getKey(e), getPack(e)));
		System.out.println(oldKvs);

		// 新的
		Set<String> newFiles = new HashSet<>();
		listJavaAndXmlFiles(newFiles, newPath);

		// AppUser -> com.markerhub.auth.entity.AppUser
		Map<String, String> newKvs = new HashMap<>();
		newFiles.forEach(e -> newKvs.put(getKey(e), getPack(e)));

		System.out.println("------------开始处理--------------");

		for (String newFile : newFiles) {
			try {
				// 创建文件读取流和写入流
				BufferedReader reader = new BufferedReader(new FileReader(newFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(newFile + ".tmp"));

				boolean hasChange = false;

				String line;
				// 逐行读取文件内容，直到读取到末尾为止
				while ((line = reader.readLine()) != null) {
					String newLine = line;

					if (newFile.endsWith(".java")) {
						// 输出每一行的内容
						if (line.startsWith("import")) {
							String clazz = line.substring(line.lastIndexOf(".") + 1, line.length() - 1);

							if (oldKvs.containsKey(clazz) && newKvs.containsKey(clazz)) {
								newLine = line.replace(oldKvs.get(clazz), newKvs.get(clazz));
								System.out.println(line + " --> " + newLine);

								hasChange = true;
							}
						}
					} else if (newFile.endsWith(".xml")) {
						if (line.contains("com.markerhub")) {
							String clazz = line.substring(line.lastIndexOf("com.markerhub") + 1, line.length() - 2);
							clazz = clazz.substring(clazz.lastIndexOf(".") + 1);

							if (oldKvs.containsKey(clazz) && newKvs.containsKey(clazz)) {
								newLine = line.replace(oldKvs.get(clazz), newKvs.get(clazz));
								System.out.println(line + " --> " + newLine);

								hasChange = true;
							}
						}
					}

					// 写入新文件
					writer.write(newLine);
					writer.newLine();
				}

				// 关闭流
				reader.close();
				writer.close();

				// 删除原文件，重命名临时文件
				File originalFile = new File(newFile);
				File tempFile = new File(newFile + ".tmp");

				if (hasChange) {
					if (originalFile.delete()) {
						if (!tempFile.renameTo(originalFile)) {
							System.err.println("无法重命名临时文件：" + tempFile.getAbsolutePath() +
									"到" + originalFile.getAbsolutePath());
						}
					} else {
						System.err.println("无法删除原文件：" + originalFile.getAbsolutePath());
					}
				} else {
					tempFile.delete();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("--------------处理结束-------------");
	}

	/**
	 * 路径中提取java类的名称
	 * D:\git-hub\vueshop-cloud\shop-auth\src\main\java\com\markerhub\auth\service\impl\AppUserServiceImpl.java -> AppUserServiceImpl
	 */
	public static String getKey(String e) {
		if (e.endsWith(".java")) {
			return e.substring(e.lastIndexOf("\\") + 1, e.lastIndexOf(".java"));
		} else {
			// xml文件忽略
			return "-----";
		}
	}

	/**
	 * 绝对路径获取类包路径
	 * D:\git-hub\vueshop-cloud\shop-auth\src\main\java\com\markerhub\auth\service\impl\AppUserServiceImpl.java -> com.markerhub.auth.service.impl.AppUserServiceImpl
	 */
	public static String getPack(String e) {
		if (e.endsWith(".java")) {
			String value = e.replace(File.separator, ".");
			return value.substring(value.lastIndexOf("com.markerhub"), value.lastIndexOf(".java"));
		} else {
			// xml
			return "=====";
		}
	}

	/**
	 * 获取文件夹下所有的java或者xml文件的绝对路径
	 */
	public static void listJavaAndXmlFiles(Set<String> fileSet, String folderPath) {
		// 创建一个File对象，代表指定文件夹
		File folder = new File(folderPath);

		// 检查文件夹是否存在
		if (folder.exists() && folder.isDirectory()) {
			// 获取文件夹下所有文件和子文件夹
			File[] files = folder.listFiles();

			// 遍历文件数组
			if (files != null) {
				for (File file : files) {
					// 如果是文件夹，则递归调用listJavaFiles方法
					if (file.isDirectory()) {
						listJavaAndXmlFiles(fileSet, file.getAbsolutePath());
					} else {
						String filePath = file.getAbsolutePath();
						if (filePath.endsWith(".java") || filePath.endsWith(".xml")) {
							fileSet.add(filePath);
						}
					}
				}
			}
		} else {
			System.out.println("指定路径不是一个有效的文件夹。");
		}
	}
}
