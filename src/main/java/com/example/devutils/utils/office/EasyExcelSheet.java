package com.example.devutils.utils.office;

import com.alibaba.excel.read.listener.ReadListener;
import lombok.Data;

/**
 * Created by AMe on 2020-09-12 08:39.
 */
@Data
public class EasyExcelSheet<T> {

    private Integer sheetNo;
    private Class<T> head;
    private ReadListener<T> readListener;
}