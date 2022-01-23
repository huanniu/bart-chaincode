package com.niuford.chaincode;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class BartContractTest {
    @Nested
    class InvokeQueryAllAssetsTransaction {
        @Test
        public void whenNoAssets() {
            BartContract contract = new BartContract();
            Context context = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(context.getStub()).thenReturn(stub);
            when(stub.getStateByRange("", "")).thenReturn(null);

            Throwable thrown = catchThrowable(() -> {
                contract.queryAllArtwork(context);
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause().hasMessage("No artworks are found");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("ARTWORK_NOT_FOUND".getBytes());
        }

        @Test
        public void whenArtworkExist() throws JsonProcessingException {
            BartContract contract = new BartContract();
            Context context = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(context.getStub()).thenReturn(stub);
            when(stub.getStateByRange("", "")).thenReturn(new MockAssetResultsIterator());

            String result = contract.queryAllArtwork(context);
            assertThat(result.isEmpty()).isFalse();
        }

        private final class MockAssetResultsIterator implements QueryResultsIterator<KeyValue> {
            private final List<KeyValue> assets;

            MockAssetResultsIterator() {
                super();

                assets = new ArrayList<>();

                assets.add(new MockKeyValue("artwork-0", "{\"artworkId\":\"artwork-0\",\"imageUrl\":\"artwork0.jpg\",\"title\":\"Artwork 0\",\"description\":\"This is artwork 0\",\"dimension\":\"1x1\",\"price\":\"99999.99\",\"currentOwner\":\"System Bot\",\"highlights\":[\"Handmade\"],\"artist\":{\"name\":\"Artist 1\",\"imageUrl\":\"artist1.jpg\",\"nationality\":\"Nationality\"},\"lastModified\":\"23 January 2022\"}"));
            }

            @Override
            public Iterator<KeyValue> iterator() {
                return assets.iterator();
            }

            @Override
            public void close() throws Exception {
                // don't need to do anything here
            }
        }

        private final class MockKeyValue implements KeyValue {
            private final String key;
            private final String value;

            MockKeyValue(String key, String value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getStringValue() {
                return value;
            }

            @Override
            public byte[] getValue() {
                return value.getBytes();
            }
        }
    }
}