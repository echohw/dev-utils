package com.example.devutils.dep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by AMe on 2020-06-11 17:33.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Range<T> {

    private T from;
    private T to;
}
