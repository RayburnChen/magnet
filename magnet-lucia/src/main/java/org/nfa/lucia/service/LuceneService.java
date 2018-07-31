package org.nfa.lucia.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.stereotype.Service;

@Service
public class LuceneService {

	public void search() throws IOException {

		// 创建索引
		Path path = Paths.get("log/lucia.txt");

		// 指定索引库的存放位置Directory对象
		Directory directory = new MMapDirectory(path);

		// 指定一个标准分析器，对文档内容进行分析
		Analyzer analyzer = new StandardAnalyzer();

		// 创建indexwriterCofig对象
		// 第一个参数： Lucene的版本信息，可以选择对应的lucene版本也可以使用LATEST
		// 第二根参数：分析器对象
		IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);

		// 创建一个indexwriter对象
		IndexWriter indexWriter = new IndexWriter(directory, writerConfig);

		// 原始文档的路径
		File file = new File("E:\\programme\\searchsource");
		File[] fileList = file.listFiles();
		for (File file2 : fileList) {
			// 创建field对象，将field添加到document对象中

			// 文件名称
			String fileName = file2.getName();
			// 创建文件名域
			// 第一个参数：域的名称
			// 第二个参数：域的内容
			// 第三个参数：是否存储
			Field fileNameField = new TextField("fileName", fileName, Store.YES);

			// 文件大小域
			Field fileSizeField = new TextField("fileSize", String.valueOf(file2.getTotalSpace()), Store.YES);

			// 文件路径
			String filePath = file2.getPath();
			// 文件路径域（不分析、不索引、只存储）
			Field filePathField = new StoredField("filePath", filePath);

			// 文件内容
			String fileContent = new String(Files.readAllBytes(file2.toPath()));
			// 文件内容域
			Field fileContentField = new TextField("fileContent", fileContent, Store.YES);

			// 创建document对象
			Document document = new Document();
			document.add(fileNameField);
			document.add(fileSizeField);
			document.add(filePathField);
			document.add(fileContentField);
			// 使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
			indexWriter.addDocument(document);
		}
		// 关闭IndexWriter对象。
		indexWriter.close();

	}

}
