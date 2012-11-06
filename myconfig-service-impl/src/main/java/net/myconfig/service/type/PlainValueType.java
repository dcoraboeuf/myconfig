package net.myconfig.service.type;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("plain")
public class PlainValueType extends AbstractValueType<Object> {

	public PlainValueType() {
		super("plain", PlainValueIO.INSTANCE, PlainValueIO.INSTANCE);
	}

}
