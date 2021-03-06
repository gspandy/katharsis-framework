package io.katharsis.errorhandling.exception;

/**
 * An exception which is thrown when a relationship repository for a classes is not found in specific package
 */
public class RelationshipRepositoryNotFoundException extends KatharsisMatchingException {
    private static final String MESSAGE = "Couldn't find a relationship repository for classes %s and %s";

    public RelationshipRepositoryNotFoundException(Class<?> baseClass, Class<?> relationshipClass) {
        super(String.format(MESSAGE, baseClass.getCanonicalName(), relationshipClass.getCanonicalName()));
    }
}
