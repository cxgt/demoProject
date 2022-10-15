package com.example.demo.service.impl;


import com.example.demo.bean.ModelDes;
import com.example.demo.mapper.ModelDesMapper;
import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private ModelDesMapper mapper;

    @Override
    public ModelDes queryById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

}
