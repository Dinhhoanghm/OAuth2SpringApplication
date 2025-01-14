export type UserRole = 'user' | 'admin' | 'super_admin';

export interface User {
  id: string;
  role: UserRole;
  // ... other user properties
}

export interface Subscription {
  id: string;
  userId: string;
  planId: string;
  status: 'active' | 'inactive';
  currentPeriodEnd: Date;
  createdAt: Date;
} 