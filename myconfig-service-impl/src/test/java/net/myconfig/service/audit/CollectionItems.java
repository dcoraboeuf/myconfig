package net.myconfig.service.audit;

import java.util.List;

import net.myconfig.core.model.Version;

import lombok.Data;

@Data
public class CollectionItems {
	
	private final List<Version> versions;

}
