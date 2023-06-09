package com.constellio.model.extensions.events.records;

import com.constellio.model.entities.records.Record;
import com.constellio.model.entities.records.wrappers.User;
import com.constellio.model.services.schemas.SchemaUtils;

public class RecordPhysicalDeletionValidationEvent implements RecordEvent {

	User user;

	Record record;

	public RecordPhysicalDeletionValidationEvent(Record record, User user) {
		this.record = record;
		this.user = user;
	}

	public Record getRecord() {
		return record;
	}

	public User getUser() {
		return user;
	}

	public String getSchemaTypeCode() {
		return new SchemaUtils().getSchemaTypeCode(record.getSchemaCode());
	}
}

--------------------

package com.stacktoheap.go.docker.Commands.PushCommands;

import com.stacktoheap.go.docker.Commands.Base.DockerCompositeCommand;
import com.stacktoheap.go.docker.Config;
import com.stacktoheap.go.docker.Context;

public class DockerTagAndPushCommand extends DockerCompositeCommand {

    public DockerTagAndPushCommand(Context taskContext, Config taskConfig) {
        super(taskContext, taskConfig);
    }

    @Override
    protected void setupCommands(Context taskContext, Config taskConfig) {
        runCommand(new DockerPushTagCommand(taskContext, taskConfig)).then(new DockerPushCommand(taskContext, taskConfig));
    }
}

--------------------

package com.constellio.app.modules.es.migrations;

import com.constellio.app.entities.modules.MetadataSchemasAlterationHelper;
import com.constellio.app.entities.modules.MigrationHelper;
import com.constellio.app.entities.modules.MigrationResourcesProvider;
import com.constellio.app.entities.modules.MigrationScript;
import com.constellio.app.modules.es.model.connectors.http.ConnectorHttpDocument;
import com.constellio.app.services.factories.AppLayerFactory;
import com.constellio.model.entities.Language;
import com.constellio.model.services.schemas.builders.MetadataSchemaBuilder;
import com.constellio.model.services.schemas.builders.MetadataSchemaTypesBuilder;

public class ESMigrationTo7_6_2 extends MigrationHelper implements MigrationScript {
	@Override
	public String getVersion() {
		return "7.6.2";
	}

	@Override
	public void migrate(String collection, MigrationResourcesProvider migrationResourcesProvider,
						AppLayerFactory appLayerFactory)
			throws Exception {
		new SchemaAlterationFor7_6_2(collection, migrationResourcesProvider, appLayerFactory).migrate();
	}

	static class SchemaAlterationFor7_6_2 extends MetadataSchemasAlterationHelper {

		protected SchemaAlterationFor7_6_2(String collection, MigrationResourcesProvider migrationResourcesProvider,
										   AppLayerFactory appLayerFactory) {
			super(collection, migrationResourcesProvider, appLayerFactory);
		}

		@Override
		protected void migrate(MetadataSchemaTypesBuilder typesBuilder) {
			MetadataSchemaBuilder documentDefaultSchema = typesBuilder.getSchemaType(ConnectorHttpDocument.SCHEMA_TYPE)
					.getDefaultSchema();
			documentDefaultSchema.get(ConnectorHttpDocument.CHARSET).addLabel(Language.French, migrationResourcesProvider
					.get("init.connectorHttpDocument.default.charset"));
			documentDefaultSchema.get(ConnectorHttpDocument.DIGEST).addLabel(Language.French, migrationResourcesProvider
					.get("init.connectorHttpDocument.default.digest"));
		}
	}
}

--------------------

