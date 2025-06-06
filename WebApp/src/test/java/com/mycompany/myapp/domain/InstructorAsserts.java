package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class InstructorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInstructorAllPropertiesEquals(Instructor expected, Instructor actual) {
        assertInstructorAutoGeneratedPropertiesEquals(expected, actual);
        assertInstructorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInstructorAllUpdatablePropertiesEquals(Instructor expected, Instructor actual) {
        assertInstructorUpdatableFieldsEquals(expected, actual);
        assertInstructorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInstructorAutoGeneratedPropertiesEquals(Instructor expected, Instructor actual) {
        assertThat(actual)
            .as("Verify Instructor auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInstructorUpdatableFieldsEquals(Instructor expected, Instructor actual) {
        assertThat(actual)
            .as("Verify Instructor relevant properties")
            .satisfies(a -> assertThat(a.getFirstName()).as("check firstName").isEqualTo(expected.getFirstName()))
            .satisfies(a -> assertThat(a.getLastName()).as("check lastName").isEqualTo(expected.getLastName()))
            .satisfies(a -> assertThat(a.getEmail()).as("check email").isEqualTo(expected.getEmail()))
            .satisfies(a -> assertThat(a.getBio()).as("check bio").isEqualTo(expected.getBio()))
            .satisfies(a -> assertThat(a.getHireDate()).as("check hireDate").isEqualTo(expected.getHireDate()))
            .satisfies(a -> assertThat(a.getRating()).as("check rating").isEqualTo(expected.getRating()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInstructorUpdatableRelationshipsEquals(Instructor expected, Instructor actual) {
        // empty method
    }
}
