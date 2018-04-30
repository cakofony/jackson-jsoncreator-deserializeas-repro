package net.ckozak;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonCreatorTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Fails: Disregards the @JsonDeserialize annotation, uses the default ArrayList
    @Test
    public void testWrapper() throws Exception {
        String json = "[\"Hello, World!\"]";
        StringListWrapper expected = new StringListWrapper(Collections.singletonList("Hello, World!"));
        StringListWrapper actual = MAPPER.readValue(json, StringListWrapper.class);
        assertEquals(expected, actual);
        assertEquals(LinkedList.class, actual.get().getClass());
    }

    // Works as expected
    @Test
    public void testWrapperWithProperty() throws Exception {
        String json = "{\"value\": [\"Hello, World!\"]}";
        StringListPropertyWrapper expected = new StringListPropertyWrapper(Collections.singletonList("Hello, World!"));
        StringListPropertyWrapper actual = MAPPER.readValue(json, StringListPropertyWrapper.class);
        assertEquals(expected, actual);
        assertEquals(LinkedList.class, actual.get().getClass());
    }

    public static final class StringListWrapper {
        private final List<String> value;

        @JsonCreator
        public StringListWrapper(@JsonDeserialize(as = LinkedList.class) List<String> value) {
            this.value = value;
        }

        @JsonValue
        public List<String> get() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StringListWrapper that = (StringListWrapper) o;

            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "StringListWrapper{" +
                    "value=" + value +
                    '}';
        }
    }

    public static final class StringListPropertyWrapper {
        private final List<String> value;

        @JsonCreator
        public StringListPropertyWrapper(@JsonProperty("value") @JsonDeserialize(as = LinkedList.class) List<String> value) {
            this.value = value;
        }

        @JsonProperty("value")
        public List<String> get() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StringListPropertyWrapper that = (StringListPropertyWrapper) o;

            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "StringListPropertyWrapper{" +
                    "value=" + value +
                    '}';
        }
    }
}
