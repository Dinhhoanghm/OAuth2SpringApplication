import { create } from 'zustand';
import type { UserRole } from '@/types';

interface User {
  id: string;
  email: string;
  role: UserRole;
}

interface AuthState {
  isAuthenticated: boolean;
  user: User | null;
  login: (user: User) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  isAuthenticated: false,
  user: null,
  login: (user) => {
    // Immediately redirect user role to marketplace without storing state
    if (user.role === 'user') {
      window.location.replace('http://localhost:8080/aiv');
      return;
    }
    // Only store auth state for admin and super_admin
    set({ isAuthenticated: true, user });
  },
  logout: () => set({ isAuthenticated: false, user: null }),
}));
