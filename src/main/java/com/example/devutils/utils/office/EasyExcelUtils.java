package com.example.devutils.utils.office;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * Created by AMe on 2020-06-25 07:51.
 */
public class EasyExcelUtils {

    public static <T> List<T> read(File srcFile, Class<T> clazz) {
        ArrayList<T> dataList = new ArrayList<>();
        read(srcFile, clazz, 0, new AnalysisEventListener<T>() {
            @Override
            public void invoke(T t, AnalysisContext analysisContext) {
                dataList.add(t);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
        });
        return dataList;
    }

    public static <T> void read(File srcFile, Class<T> clazz, Integer sheetNo, ReadListener<T> readListener) {
        ExcelReader excelReader = EasyExcel.read(srcFile, clazz, readListener).build();
        ReadSheet readSheet = EasyExcel.readSheet(sheetNo).build();
        excelReader.read(readSheet);
        excelReader.finish();
    }

    public static <T> void readAllSheet(File srcFile, Class<T> clazz, ReadListener<T> readListener) {
        EasyExcel.read(srcFile, clazz, readListener).doReadAll();
    }

    @Data
    public static class Sheet<T> {

        private Integer sheetNo;
        private Class<T> clazz;
        private ReadListener<T> readListener;
    }

    public static <T> void readAllSheet(InputStream srcInput, List<Sheet> sheets) {
        ExcelReader excelReader = EasyExcel.read(srcInput).build();
        List<ReadSheet> readSheetList = sheets.stream().map(sheet ->
            EasyExcel
                .readSheet(sheet.getSheetNo())
                .head(sheet.getClazz())
                .registerReadListener(sheet.getReadListener())
                .build()
        ).collect(Collectors.toList());
        excelReader.read(readSheetList);
        excelReader.finish();
    }

    public static <T> void write(File destFile, Class<T> clazz, String sheetName, List<T> dataList) {
        EasyExcel.write(destFile, clazz).sheet(sheetName).doWrite(dataList);
    }

    public static <T> void write(OutputStream destOutput, Class<T> clazz, Collection<String> includeFields, Collection<String> excludeFields, String sheetName, List<T> dataList) {
        EasyExcel.write(destOutput, clazz)
            .includeColumnFiledNames(includeFields)
            .excludeColumnFiledNames(excludeFields)
            .sheet(sheetName)
            .doWrite(dataList);
    }

    public static <T> void write(File destFile, File templateFile, String sheetName, List<T> dataList) {
        EasyExcel.write(destFile).withTemplate(templateFile).sheet(sheetName).doFill(dataList);
    }

    public static <T> void write(OutputStream destOutput, InputStream templateInput, String sheetName, List<T> dataList) {
        EasyExcel.write(destOutput).withTemplate(templateInput).sheet(sheetName).doFill(dataList);
    }

}
