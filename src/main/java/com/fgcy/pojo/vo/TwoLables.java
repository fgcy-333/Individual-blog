package com.fgcy.pojo.vo;

import com.fgcy.pojo.Tag;
import com.fgcy.pojo.Type;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/22
 */
public class TwoLables {
    private List<Type> types;
    private List<Tag> tags;

    @Override
    public String toString() {
        return "TowLables{" +
                "types=" + types +
                ", tags=" + tags +
                '}';
    }

    public List<Type> getTypes() {
        return types;
    }

    public TwoLables setTypes(List<Type> types) {
        this.types = types;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public TwoLables setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }
}
