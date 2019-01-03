package processor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Arguments {

	private Path templateSourceRootFolder;

	private Path featureDestinationRootFolder;

	public static final String DEFAULT_TEMPLATE_SOURCE_ROOT = "src/test/resources/template/";

	public static final String DEFAULT_FEATURE_DESTINATION_ROOT = "src/test/resources/feature/";

	public Arguments() {}

	public Arguments(String tempSrcFld, String featDestFld) {
		this.templateSourceRootFolder = Paths.get(tempSrcFld);
		this.featureDestinationRootFolder = Paths.get(featDestFld);
	}

	public Arguments(Path tempSrcFld, Path featDestFld) {
		this.templateSourceRootFolder = tempSrcFld;
		this.featureDestinationRootFolder = featDestFld;
	}

	public Arguments(String[] args) {
		processArguments(args);
	}

	private void processArguments(String[] args) {
		String source = (args.length == 2) ? args[0] : DEFAULT_TEMPLATE_SOURCE_ROOT;
		String destination = (args.length == 2) ? args[1] : DEFAULT_FEATURE_DESTINATION_ROOT;

		this.templateSourceRootFolder = Paths.get(source);
		this.featureDestinationRootFolder = Paths.get(destination);
	}

	public Path getTemplateSourceRootFolder() {
		return templateSourceRootFolder;
	}

	public Path getFeatureDestinationRootFolder() {
		return featureDestinationRootFolder;
	}

}
