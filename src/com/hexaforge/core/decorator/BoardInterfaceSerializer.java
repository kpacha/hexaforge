package com.hexaforge.core.decorator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hexaforge.core.board.BoardBuilder;
import com.hexaforge.core.board.BoardImplementationEnum;
import com.hexaforge.core.cell.CellFactory;
import com.hexaforge.core.interfaces.BoardInterface;
import com.hexaforge.core.interfaces.CellInterface;

public class BoardInterfaceSerializer implements
		JsonSerializer<BoardInterface>, JsonDeserializer<BoardInterface> {

	public JsonElement serialize(BoardInterface src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.addProperty("maxX", src.getMaxX());
		result.addProperty("maxY", src.getMaxY());
		result.addProperty("type", src.getBoardType().name());
		result.add("cell", serializeCells(src.getCells()));
		return result;
	}

	public BoardInterface deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		try {
			JsonObject obj = (JsonObject) json;
			BoardImplementationEnum boardType = BoardImplementationEnum
					.valueOf(BoardImplementationEnum.class, obj.get("type").getAsString());
			BoardInterface board = BoardBuilder.makeBoard(boardType,
					obj.get("maxX").getAsInt(), obj.get("maxY").getAsInt());
			board.setCells(deserializeCells(obj.get("cell").getAsJsonArray()));
			return board;
		} catch (Exception e) {
			throw new JsonParseException(e.getMessage());
		}
	}

	protected List<CellInterface> deserializeCells(JsonArray serializedCells) {
		List<CellInterface> cells = new ArrayList<CellInterface>();
		CellInterface cell;
		JsonObject serializedCell;
		for (Iterator<JsonElement> iter = serializedCells.iterator(); iter
				.hasNext();) {
			serializedCell = (JsonObject) iter.next();
			cell = CellFactory.makeCell(serializedCell.get("code")
					.getAsCharacter(), serializedCell.get("x").getAsInt(),
					serializedCell.get("y").getAsInt(),
					serializedCell.get("owner").getAsInt());
			cells.add(cell);
		}
		return cells;
	}

	protected JsonElement serializeCells(List<CellInterface> cells) {
		JsonArray cellArray = new JsonArray();
		CellInterface cell;
		JsonObject serializedCell;
		for (Iterator<CellInterface> iter = cells.iterator(); iter.hasNext();) {
			cell = iter.next();
			serializedCell = new JsonObject();
			serializedCell.addProperty("code", cell.getCode().getCode());
			serializedCell.addProperty("x", cell.getX());
			serializedCell.addProperty("y", cell.getY());
			serializedCell.addProperty("owner", cell.getOwner());
			serializedCell.addProperty("range", cell.getRange());
			cellArray.add(serializedCell);
		}
		return cellArray;
	}
}
