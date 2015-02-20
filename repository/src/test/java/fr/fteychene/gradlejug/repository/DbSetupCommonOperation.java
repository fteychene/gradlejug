package fr.fteychene.gradlejug.repository;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;

/**
 * Created by fteychene on 06/02/2015.
 */
public final class DbSetupCommonOperation {

    public static final Operation DELETE_ALL =
            deleteAllFrom("TODO");
}
