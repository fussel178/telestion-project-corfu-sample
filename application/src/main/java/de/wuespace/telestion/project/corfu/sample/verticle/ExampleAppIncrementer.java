package de.wuespace.telestion.project.corfu.sample.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.TelestionConfiguration;
import de.wuespace.telestion.api.verticle.TelestionVerticle;
import de.wuespace.telestion.api.verticle.trait.WithEventBus;
import de.wuespace.telestion.api.verticle.trait.WithTiming;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuTelecommand;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp.ExampleAppTelecommand;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp.IncrementCounterTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.node.obc.ObcNode;

import java.time.Duration;

@SuppressWarnings("unused")
public class ExampleAppIncrementer extends TelestionVerticle<ExampleAppIncrementer.Configuration>
		implements WithEventBus, WithTiming {

	public record Configuration(
			@JsonProperty String outAddress,
			@JsonProperty int interval,
			@JsonProperty short step
	) implements TelestionConfiguration {
		public Configuration() {
			this(null, 5000, (short) 1);
		}
	}

	@Override
	public void onStart() {
		var telecommand = new CorfuTelecommand(
				(short) 0,
				(short) 0,
				// execute immediate
				0L,
				// only to obc node
				new ObcNode(ObcNode.ObcHardware.MT0_ID0),
				// to example app with command increment counter
				new ExampleAppTelecommand(new IncrementCounterTelecommandPayload(getConfig().step()))
		);

		interval(Duration.ofMillis(getConfig().interval), timerId -> publish(getConfig().outAddress, telecommand));
	}
}
