# IsleLink

A lightweight Android utility to open web links from your "Work Profile" directly in your "Personal Profile" browser.

## Features

- Intercepts link clicks inside the Work Profile.
- Dispatches URLs across profiles seamlessly to open them in your personal default browser.
- Two distinct implementation are in this repository.

## Architecture: Classic vs. Next

| Feature | Classic | Next |
| :--- | :--- | :--- |
| **Module** | `islelink.dispatcher` / `islelink.receiver` | `islelink.next` |
| **Min Android** | Android 5.0+ (API 21) | Android 11.0+ (API 30) |
| **Core API** | Deep Link / Custom Scheme (`islelink://`) | `CrossProfileApps` (Native API) |
| **Mechanism** | Encapsulates URL in a bridge scheme with multi-tier fallback | Directly targets personal profile via `targetUserProfiles` |
| **Design** | Hacky workaround for legacy compatibility | Clean, deterministic, zero URL transformation |

## How to Use (Next)

1. Build and install the app in **both** the Personal Profile and Work Profile.
2. Inside your Work Profile, set IsleLink as the default browser (or choose it when clicking links).
3. Links clicked inside the Work Profile will instantly launch in your Personal Profile browser.

## License

MIT
