package com.atguigu.lease.web.admin.custom.converter;

import com.atguigu.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

//@Component
public class StringToItemTypeConverter implements Converter<String, ItemType> {

    @Override
    public ItemType convert(String code) {
        ItemType[] types = ItemType.values();
        for (ItemType type : types) {
            if (type.getCode().equals(Integer.valueOf(code))){
                return type;
            }
        }
        throw  new IllegalArgumentException("code"+code+"非法");
    }

}
