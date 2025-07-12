package br.edu.ifsp.dsw.myfinanceapi.model.adapter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import br.edu.ifsp.dsw.myfinanceapi.model.enums.TransactionType;

public class TransactionAdapter extends TypeAdapter<Transaction> {

	@Override
	public void write(JsonWriter out, Transaction value) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public Transaction read(JsonReader in) throws IOException {
		JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dueDate;
		
		try {
			dueDate = dateFormat.parse(jsonObject.get("dueDate").getAsString());
		} catch (ParseException e) {
			throw new JsonParseException("Invalid date format", e);
		}

		Transaction transaction = new Transaction();
		transaction.setDescription(jsonObject.get("description").getAsString());
		transaction.setValue(new BigDecimal(jsonObject.get("value").getAsString()));
		transaction.setType(TransactionType.valueOf(jsonObject.get("type").getAsString()));
		transaction.setDueDate(dueDate);
		
		if (jsonObject.has("category") && StringUtils.isNotBlank(jsonObject.get("category").getAsString())) {
			Category category = new Category();
			category.setId(jsonObject.get("category").getAsInt());
			transaction.setCategory(category);
		}

		return transaction;
	}

}
