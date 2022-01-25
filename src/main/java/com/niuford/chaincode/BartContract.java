package com.niuford.chaincode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Contract(
        name = "bart-contract",
        info = @Info(
                title = "Bart Contract",
                description = "The contract is to define how to interact with Bart blockchain network",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                ),
                contact = @Contact(
                        email = "admin@bart.com",
                        name = "Blockchain Artwork Ltd.",
                        url = "https://bart.com"
                )
        )
)

@Default
public class BartContract implements ContractInterface {
    private final static String DATA_PATTERN = "dd MMMMM yyyy";

    private final Logger logger = Logger.getLogger(getClass());

    private enum BartError {
        ARTWORK_NOT_FOUND,
        ARTWORK_ALREADY_EXISTS
    }

    @Transaction()
    public void initLedger(final Context context) throws JsonProcessingException {
        ChaincodeStub stub = context.getStub();

        String artworkId = "artwork-0";

        ArtistState artist = new ArtistState("Artist 1", "artist1.jpg", "Nationality");
        ArtworkState artworkState = new ArtworkState(artworkId, "artwork0.jpg", "Artwork 0", "This is artwork 0",
                "1x1", "99999.99", "System Bot", List.of("Handmade"), artist, new Date());

        ObjectMapper objectMapper = new ObjectMapper();
        String artworkInJson = objectMapper.writeValueAsString(artworkState);

        stub.putStringState(artworkId, artworkInJson);
    }

    @Transaction
    public String queryAllArtwork(final Context context) throws JsonProcessingException {
        ChaincodeStub stub = context.getStub();

        List<ArtworkState> allArtworkStates = new ArrayList<>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        ObjectMapper objectMapper = new ObjectMapper();

        if (results != null) {
            logger.info("Artwork states are received.");
            for (KeyValue result: results) {
                logger.info("Artwork will be returned: " + result.getStringValue());
                ArtworkState artworkState = objectMapper.readValue(result.getStringValue(), ArtworkState.class);
                logger.info("Artwork is deserialized: " + artworkState);
                allArtworkStates.add(artworkState);
            }
        } else {
            String errorMessage = "No artworks are found";
            logger.error(errorMessage);
            throw new ChaincodeException(errorMessage, BartError.ARTWORK_NOT_FOUND.toString());
        }

        return objectMapper.writeValueAsString(allArtworkStates);
    }

    @Transaction
    public String queryArtwork(final Context context, final String key) throws JsonProcessingException {
        ChaincodeStub stub = context.getStub();

        String stateInJson = stub.getStringState(key);

        if (stateInJson.isEmpty()) {
            String errorMessage = String.format("Artwork %s does not exist", key);
            logger.error(errorMessage);
            throw new ChaincodeException(errorMessage, BartError.ARTWORK_NOT_FOUND.toString());
        }

        return stateInJson;
    }

    @Transaction
    public void createArtwork(final Context context, final String artworkInJson) throws JsonProcessingException {
        ChaincodeStub stub = context.getStub();

        ObjectMapper objectMapper = new ObjectMapper();

        ArtworkState artworkState = objectMapper.readValue(artworkInJson, ArtworkState.class);
        String artworkId = artworkState.getArtworkId();

        String stateInBlockchain = stub.getStringState(artworkId);
        if (!stateInBlockchain.isEmpty()) {
            String errorMessage = String.format("Artwork %s already exists", artworkId);
            logger.error(errorMessage);
            throw new ChaincodeException(errorMessage, BartError.ARTWORK_ALREADY_EXISTS.toString());
        }

        stub.putStringState(artworkId, artworkInJson);
    }

    @Transaction
    public void transferArtwork(final Context context, final String artworkId, final String newOwner) throws JsonProcessingException {
        ChaincodeStub stub = context.getStub();

        String artworkInJson = stub.getStringState(artworkId);
        if (artworkInJson.isEmpty()) {
            String errorMessage = String.format("Artwork %s does not exist", artworkId);
            logger.error(errorMessage);
            throw new ChaincodeException(errorMessage, BartError.ARTWORK_NOT_FOUND.toString());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ArtworkState artworkState = objectMapper.readValue(artworkInJson, ArtworkState.class);

        ArtworkState newArtworkState = new ArtworkState(artworkId, artworkState.getImageUrl(), artworkState.getTitle(), artworkState.getDescription(),
                artworkState.getDimension(), artworkState.getPrice(), newOwner, artworkState.getHighlights(), artworkState.getArtist(), new Date());

        String newArtworkStateInJson = objectMapper.writeValueAsString(newArtworkState);
        stub.putStringState(artworkId, newArtworkStateInJson);
    }

    @Transaction
    public String transactionHistoryForArtworkId(final Context context, final String artworkId) throws JsonProcessingException {
        ChaincodeStub stub = context.getStub();
        QueryResultsIterator<KeyModification> historyForKey = stub.getHistoryForKey(artworkId);

        final List<ArtworkState> history = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        if (historyForKey != null) {
            for (KeyModification km: historyForKey) {
                history.add(objectMapper.readValue(km.getStringValue(), ArtworkState.class));
            }
            return objectMapper.writeValueAsString(history);
        } else {
            return objectMapper.writeValueAsString(new ArrayList<ArtworkState>());
        }
    }
}
