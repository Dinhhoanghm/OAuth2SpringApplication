import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { PlusCircle, Pencil, Trash2 } from 'lucide-react';
import { useState, useEffect } from 'react';
import axios from 'axios';
import axiosInstance from '../axios';

export default function SettingsPage() {
  const [users, setUsers] = useState<any[]>([]);
  const [isAddingUser, setIsAddingUser] = useState(false);
  const [isEditingUser, setIsEditingUser] = useState(false);
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [newUser, setNewUser] = useState({ last_name: '', email: '', role: '', password: '' });

  const hardcodedUsers = [
    { id: '1', name: 'John Doe', email: 'john@example.com', role: 'Admin', status: 'Active', password: 'password123' },
    { id: '2', name: 'Jane Smith', email: 'jane@example.com', role: 'User', status: 'Active', password: 'password123' },
  ];

  // Fetch users from the API with fallback to hardcoded users
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axiosInstance.get('/api/user/getAll');
        setUsers(response.data || hardcodedUsers);
      } catch (error) {
        console.error('Error fetching users:', error);
        setUsers(hardcodedUsers); // Use hardcoded users in case of error
      }
    };

    fetchUsers();
  }, []);

  // Handle Add User Form Submission
  const handleAddUser = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log('token', localStorage.getItem('token'));
    try {
      const response = await axiosInstance.post('/api/user/insert', newUser);
      setUsers((prevUsers) => [...prevUsers, response.data]);
      setNewUser({ last_name: '', email: '', role: '', password: '' });
      setIsAddingUser(false);
    } catch (error) {
      console.error('Error adding user:', error);
    }
  };

  // Handle User Deletion
  const handleDeleteUser = async (user: any) => {
    try {
      await axiosInstance.delete(`/api/user/delete`, { data: user });
      setUsers((prevUsers) => prevUsers.filter((u) => u.id !== user.id));
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  };

  // Handle Update User Form Submission
  const handleUpdateUser = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await axiosInstance.post(`/api/user/update`, currentUser);
      setUsers((prevUsers) => prevUsers.map((user) => (user.id === currentUser.id ? response.data : user)));
      setIsEditingUser(false);
      setCurrentUser(null);
    } catch (error) {
      console.error('Error updating user:', error);
    }
  };

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Settings</h1>

      <Tabs defaultValue="users" className="w-full">
        <TabsList className="w-full justify-start border-b rounded-none h-auto p-0 bg-transparent">
          {['Users'].map((tab) => (
            <TabsTrigger
              key={tab.toLowerCase()}
              value={tab.toLowerCase()}
              className="data-[state=active]:bg-transparent data-[state=active]:shadow-none data-[state=active]:border-primary data-[state=active]:border-b-2 rounded-none px-4 py-2 h-10"
            >
              {tab}
            </TabsTrigger>
          ))}
        </TabsList>

        <div className="mt-6">
          <TabsContent value="users">
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <h3 className="text-lg font-medium">User Management</h3>
                <Button onClick={() => setIsAddingUser(true)}>
                  <PlusCircle className="h-4 w-4 mr-2" />
                  Add User
                </Button>
              </div>

              {(isAddingUser || isEditingUser) && (
                <Card>
                  <div className="p-6">
                    <h3 className="text-lg font-medium">{isEditingUser ? 'Edit User' : 'Add New User'}</h3>
                    <form onSubmit={isEditingUser ? handleUpdateUser : handleAddUser} className="mt-4 space-y-4">
                      <div className="space-y-2">
                        <Label htmlFor="name">Name</Label>
                        <Input
                          id="name"
                          value={isEditingUser ? currentUser?.last_name : newUser.last_name}
                          onChange={(e) =>
                            isEditingUser
                              ? setCurrentUser({ ...currentUser, last_name: e.target.value })
                              : setNewUser({ ...newUser, last_name: e.target.value })
                          }
                          required
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="email">Email</Label>
                        <Input
                          id="email"
                          type="email"
                          value={isEditingUser ? currentUser?.email : newUser.email}
                          onChange={(e) =>
                            isEditingUser
                              ? setCurrentUser({ ...currentUser, email: e.target.value })
                              : setNewUser({ ...newUser, email: e.target.value })
                          }
                          required
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="role">Role</Label>
                        <Input
                          id="role"
                          value={isEditingUser ? currentUser?.role : newUser.role}
                          onChange={(e) =>
                            isEditingUser
                              ? setCurrentUser({ ...currentUser, role: e.target.value })
                              : setNewUser({ ...newUser, role: e.target.value })
                          }
                          required
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="password">Password</Label>
                        <Input
                          id="password"
                          type="password"
                          value={isEditingUser ? currentUser?.password : newUser.password}
                          onChange={(e) =>
                            isEditingUser
                              ? setCurrentUser({ ...currentUser, password: e.target.value })
                              : setNewUser({ ...newUser, password: e.target.value })
                          }
                          required={!isEditingUser}
                        />
                      </div>
                      <div className="flex justify-end space-x-2">
                        <Button
                          variant="outline"
                          onClick={() => {
                            setIsAddingUser(false);
                            setIsEditingUser(false);
                            setCurrentUser(null);
                          }}
                        >
                          Cancel
                        </Button>
                        <Button type="submit">{isEditingUser ? 'Update User' : 'Add User'}</Button>
                      </div>
                    </form>
                  </div>
                </Card>
              )}

              <Card>
                <div className="p-6">
                  <div className="rounded-md border">
                    <table className="min-w-full divide-y divide-gray-200">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Name
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Email
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Role
                          </th>
                          <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Actions
                          </th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {Array.isArray(users) &&
                          users.map((user: any) => (
                            <tr key={user.id}>
                              <td className="px-6 py-4 whitespace-nowrap">
                                <div className="font-medium">{user.last_name}</div>
                              </td>
                              <td className="px-6 py-4 whitespace-nowrap text-sm">{user.email}</td>
                              <td className="px-6 py-4 whitespace-nowrap text-sm">{user.role}</td>
                              <td className="px-6 py-4 whitespace-nowrap text-right text-sm">
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="mr-2"
                                  onClick={() => {
                                    setIsEditingUser(true);
                                    setCurrentUser(user);
                                  }}
                                >
                                  <Pencil className="h-4 w-4" />
                                </Button>
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="text-red-600 hover:text-red-700 hover:bg-red-50"
                                  onClick={() => handleDeleteUser(user)}
                                >
                                  <Trash2 className="h-4 w-4" />
                                </Button>
                              </td>
                            </tr>
                          ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              </Card>
            </div>
          </TabsContent>
        </div>
      </Tabs>
    </div>
  );
}
