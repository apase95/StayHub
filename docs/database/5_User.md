# User

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | User identifier |
| `email` | VARCHAR(255) | NOT NULL, UNIQUE | Login email |
| `username` | VARCHAR(80) | UNIQUE | Username for local accounts and display-friendly login identity |
| `password_hash` | VARCHAR(255) | NOT NULL | BCrypt password hash |
| `full_name` | VARCHAR(150) | NOT NULL | User full name |
| `phone` | VARCHAR(30) | NULL | Contact phone |
| `role` | VARCHAR(20) | NOT NULL | `GUEST`, `HOST`, `ADMIN` |
| `status` | VARCHAR(20) | NOT NULL | `ACTIVE`, `LOCKED`, `INACTIVE` |
| `login_provider` | VARCHAR(20) | NOT NULL | `LOCAL`, `GOOGLE` |
| `provider_id` | VARCHAR(255) | NULL | External provider subject/id for OAuth2 accounts |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

### Recommended constraints

```sql
CONSTRAINT chk_users_role
CHECK (role IN ('GUEST', 'HOST', 'ADMIN'));

CONSTRAINT chk_users_status
CHECK (status IN ('ACTIVE', 'LOCKED', 'INACTIVE'));

CONSTRAINT chk_users_login_provider
CHECK (login_provider IN ('LOCAL', 'GOOGLE'));
```

### Authentication notes

- Local registration uses an email OTP flow: user data is held temporarily in memory until OTP verification succeeds, then a `LOCAL` user is created.
- Google OAuth2 login creates or updates a `GOOGLE` user using the Google `sub` as `provider_id`.
- Email is normalized before persistence and remains the primary unique account identifier.

### Relationship

```text
users
 ├── 1 : N properties
 ├── 1 : N bookings
 └── 1 : N reviews
```
