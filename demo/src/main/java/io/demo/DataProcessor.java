package io.demo;

import model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class DataProcessor implements ItemProcessor<DataInput1, Data> {

    private static final Logger log = LoggerFactory.getLogger(DataProcessor.class);

    @Override
    public Data process(DataInput1 item) throws Exception {
        Data data = new Data();
        data.setEnd_year(item.getEnd_year());
        data.setIntensity(item.getIntensity());
        data.setCity(item.getCity());
        data.setCountry(item.getCountry());
        data.setRelevance(item.getRelevance());
        data.setLikelihood(item.getLikelihood());
        data.setTopic(item.getTopic());
        data.setRegion(item.getRegion());
        data.setStart_year(item.getStart_year());
        return data;
    }
}



