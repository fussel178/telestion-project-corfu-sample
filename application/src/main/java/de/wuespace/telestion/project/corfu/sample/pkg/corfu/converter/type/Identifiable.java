package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Objects;

/**
 * An identifiable is a model in a Corfu configuration file which has an id, a name and an optional description.
 * Such models are:
 * <ul>
 *     <li>{@link AppConfiguration}</li>
 *     <li>{@link NodeConfiguration}</li>
 *     <li>{@link Message}</li>
 * </ul>
 *
 * @author Ludwig Richter (@fussel178)
 */
@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifiable {

	public static final short MIN_ID = 1;

	public static final short MAX_ID = 255;

	protected ComponentName name;

	protected String description;

	protected short id;

	public Identifiable() {
		// default values
		this.name = null;
		this.description = null;
		this.id = 0;
	}

	@JsonSetter("name")
	public void setName(String raw) {
		this.name = new ComponentName(raw);
	}

	public ComponentName getName() {
		return name;
	}

	@JsonSetter("description")
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return Objects.requireNonNullElse(description, "N/A");
	}

	public boolean hasDescription() {
		return Objects.nonNull(description);
	}

	@JsonSetter("id")
	public void setId(short id) {
		this.id = id;
	}

	public short getId() {
		if (id < MIN_ID || id > MAX_ID) {
			throw new IllegalArgumentException(("The id of the identifiable %s is not valid. Please provide a valid " +
					"id and try again. Got: 0x%02X, Expected: 0x%02X - 0x%02X").formatted(name, id, MIN_ID, MAX_ID));
		}

		return id;
	}

	@Override
	public String toString() {
		return "Identifiable{" + "id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
