package com.MyProject.DigitalBankingSystem.config;

import org.modelmapper.ModelMapper;

public class ModelMapperConfig {
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}