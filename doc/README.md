# Coffee Champ API

### Get Recommendation
Returns JSON with recommendation and preferences used to generate it.

### Url: `/create-recommendation`

### Methods: `POST`

### Payload:
```json
{ "items": ["String"] }
```

### Success Response:
* Code: `200`
* Content: `{ "recommendation": "String", "preferences": ["String"] }`