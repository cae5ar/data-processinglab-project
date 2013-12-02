package com.pstu.dtl.shared.qbe;

import com.pstu.dtl.shared.dto.IDto;

@SuppressWarnings("serial")
public class AbstractQBE implements IDto {

    private int first = 0;
    private int count = -1;

    public AbstractQBE() {
        super();
    }

    public AbstractQBE(int first, int count) {
        super();
        this.first = first;
        this.count = count;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
