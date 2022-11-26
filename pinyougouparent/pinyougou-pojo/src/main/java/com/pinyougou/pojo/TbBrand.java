package com.pinyougou.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "Tb_Brand")
@Entity
public class TbBrand implements Serializable {
    @Id
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "first_char")
    private String firstChar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }

    /**
     * 前台select2数据初始化使用
     * @return
     */
    public String getText()
    {
        return name;
    }

    @Override
    public String toString() {
        return "TbBrand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstChar='" + firstChar + '\'' +
                '}';
    }
}