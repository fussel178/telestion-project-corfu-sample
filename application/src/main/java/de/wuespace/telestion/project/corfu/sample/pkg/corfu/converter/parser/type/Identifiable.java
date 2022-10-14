package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifiable {
	public Identifiable() {
		// default values
		this.name = null;
		this.description = null;
		this.id = 0;
	}

	/**
	 * The name of the model.
	 */
	@JsonProperty
	public String name;

	/**
	 * The optional description of the model.
	 * <p>
	 * Note: Check with {@link #hasDescription()} method if the configuration provides a valid description.
	 */
	@JsonProperty
	public String description;

	/**
	 * The unique id of the model.
	 * <p>
	 * Note: Check with {@link #hasId()} method if the configuration provides a valid id.
	 */
	@JsonProperty
	public short id;

	public boolean hasDescription() {
		return Objects.nonNull(description);
	}

	public boolean hasId() {
		return id > 0;
	}

	@Override
	public String toString() {
		return "Identifiable{" + "id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
