@TypeDefs({
    @TypeDef(
        name = "collaboration",
        typeClass = GenericEnumUserType.class,
        defaultForType = Collaboration.class,
        parameters = {
            @Parameter(name = "enumClass", value = "edu.ualberta.cs.papersdb.model.Collaboration")
        }),
    @TypeDef(
        name = "ranking",
        typeClass = GenericEnumUserType.class,
        defaultForType = Ranking.class,
        parameters = {
            @Parameter(name = "enumClass", value = "edu.ualberta.cs.papersdb.model.Ranking")
        })
})
package edu.ualberta.cs.papersdb.model;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import edu.ualberta.cs.papersdb.model.util.GenericEnumUserType;

