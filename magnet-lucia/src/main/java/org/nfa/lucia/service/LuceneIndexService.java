package org.nfa.lucia.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;

import javax.annotation.PreDestroy;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class LuceneIndexService {

	@Autowired
	private IndexWriter indexWriter;

	@PreDestroy
	public void preDestroy() {
		try {
			indexWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void build() {
		try {
			// 原始文档的路径
			Path sourcePath = Paths.get(new ClassPathResource("/files").getURI());
			Files.walkFileTree(sourcePath, getFileVisitor(getConsumer(this.indexWriter)));
			// 关闭IndexWriter对象。
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Consumer<Path> getConsumer(IndexWriter indexWriter) {
		return path -> {
			try {
				File file2 = path.toFile();
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
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};
	}

	private FileVisitor<Path> getFileVisitor(Consumer<Path> consumer) {
		return new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				consumer.accept(file);
				return FileVisitResult.CONTINUE;
			}
		};
	}

}
