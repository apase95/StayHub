# User

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | User identifier |
| `email` | VARCHAR(255) | NOT NULL, UNIQUE | Login email |
| `password_hash` | VARCHAR(255) | NOT NULL | BCrypt password hash |
| `full_name` | VARCHAR(150) | NOT NULL | User full name |
| `phone` | VARCHAR(30) | NULL | Contact phone |
| `role` | VARCHAR(20) | NOT NULL | `GUEST`, `HOST`, `ADMIN` |
| `status` | VARCHAR(20) | NOT NULL | `ACTIVE`, `LOCKED`, `INACTIVE` |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

### Recommended constraints

```sql
CONSTRAINT chk_users_role
CHECK (role IN ('GUEST', 'HOST', 'ADMIN'));

CONSTRAINT chk_users_status
CHECK (status IN ('ACTIVE', 'LOCKED', 'INACTIVE'));
```

### Relationship

```text
users
 ├── 1 : N properties
 ├── 1 : N bookings
 └── 1 : N reviews
```