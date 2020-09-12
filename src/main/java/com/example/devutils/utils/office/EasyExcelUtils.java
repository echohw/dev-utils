package com.example.devutils.utils.office;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by AMe on 2020-06-25 07:51.
 */
public class EasyExcelUtils {

    public static <T> List<T> read(File inputFile, Class<T> head) {
        ArrayList<T> dataList = new ArrayList<>();
        read(inputFile, head, 0, new AnalysisEventListener<T>() {
            @Override
            public void invoke(T t, AnalysisContext analysisContext) {
                dataList.add(t);
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
        });
        return dataList;
    }

    public static <T> void read(File inputFile, Class<T> head, Integer sheetNo, ReadListener<T> readListener) {
        EasyExcel.read(inputFile, head, readListener).sheet(sheetNo).doRead();
    }

    public static <T> void read(File inputFile, Class<T> head, Integer sheetNo,
        ReadListener<T> readListener,
        Consumer<ExcelReaderBuilder> excelReaderBuilderConsumer,
        Consumer<ExcelReaderSheetBuilder> excelReaderSheetBuilderConsumer
    ) {
        ExcelReaderBuilder excelReaderBuilder = EasyExcel.read(inputFile, head, readListener);
        Optional.ofNullable(excelReaderBuilderConsumer).ifPresent(consumer -> consumer.accept(excelReaderBuilder));
        ExcelReader excelReader = excelReaderBuilder.build();
        ExcelReaderSheetBuilder excelReaderSheetBuilder = EasyExcel.readSheet(sheetNo);
        Optional.ofNullable(excelReaderSheetBuilderConsumer).ifPresent(consumer -> consumer.accept(excelReaderSheetBuilder));
        ReadSheet readSheet = excelReaderSheetBuilder.build();
        excelReader.read(readSheet);
        excelReader.finish();
    }

    public static <T> void readAllSheet(File inputFile, Class<T> head, ReadListener<T> readListener) {
        EasyExcel.read(inputFile, head, readListener).doReadAll();
    }

    public static <T> void write(File outputFile, Class<T> head, String sheetName, List<T> dataList) {
        EasyExcel.write(outputFile, head).sheet(sheetName).doWrite(dataList);
    }

    public static <T> void write(File outputFile, Class<T> head, File templateFile, String sheetName, List<T> dataList) {
        EasyExcel.write(outputFile, head).withTemplate(templateFile).sheet(sheetName).doWrite(dataList);
    }

    public static <T> void write(OutputStream outputStream, Class<T> head, String sheetName, List<T> dataList) {
        EasyExcel.write(outputStream, head).sheet(sheetName).doWrite(dataList);
    }

    public static <T> void write(OutputStream outputStream, Class<T> head, InputStream templateInputStream, String sheetName, List<T> dataList) {
        EasyExcel.write(outputStream, head).withTemplate(templateInputStream).sheet(sheetName).doWrite(dataList);
    }

    public static <T> void write(OutputStream outputStream, Class<T> head, String sheetName,
        Consumer<ExcelWriterBuilder> excelWriterBuilderConsumer,
        Consumer<ExcelWriterSheetBuilder> excelWriterSheetBuilderConsumer,
        List<T> dataList
    ) {
        write(outputStream, head, sheetName, excelWriterBuilderConsumer, excelWriterSheetBuilderConsumer, (excelWriter, writeSheet) -> excelWriter.write(dataList, writeSheet));
    }

    public static void write(OutputStream outputStream, Class<?> head, String sheetName,
        Consumer<ExcelWriterBuilder> excelWriterBuilderConsumer,
        Consumer<ExcelWriterSheetBuilder> excelWriterSheetBuilderConsumer,
        BiConsumer<ExcelWriter, WriteSheet> writeOptConsumer
    ) {
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(outputStream, head);
        Optional.ofNullable(excelWriterBuilderConsumer).ifPresent(consumer -> consumer.accept(excelWriterBuilder));
        ExcelWriter excelWriter = excelWriterBuilder.build();
        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet(sheetName);
        Optional.ofNullable(excelWriterSheetBuilderConsumer).ifPresent(consumer -> consumer.accept(excelWriterSheetBuilder));
        WriteSheet writeSheet = excelWriterSheetBuilder.build();
        writeOptConsumer.accept(excelWriter, writeSheet);
        excelWriter.finish();
    }

    public static void fill(File outputFile, File templateFile, String sheetName, Object data) {
        EasyExcel.write(outputFile).withTemplate(templateFile).sheet(sheetName).doFill(data);
    }

    public static void fill(OutputStream outputStream, InputStream templateInputStream, String sheetName, Object data) {
        EasyExcel.write(outputStream).withTemplate(templateInputStream).sheet(sheetName).doFill(data);
    }
}
