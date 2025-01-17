import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { PlusCircle, Pencil, Trash2 } from 'lucide-react';
import { useState, useEffect } from 'react';
import axios from 'axios';
import axiosInstance from '../axios';
import { Switch } from '../components/ui/switch';
import { CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { formatCurrency } from '../lib/utils';

interface Billing {
  id: number;
  plan_user_id: number;
  status: string;
  created_at: string; // or use `LocalDateTime` if your app supports it
  paid_at: string; // or use `LocalDateTime`
  amount: number;
  session_id: string;
}

export default function SettingsPage() {
  const [users, setUsers] = useState<any[]>([]);
  const [isAddingUser, setIsAddingUser] = useState(false);
  const [isEditingUser, setIsEditingUser] = useState(false);
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [billingHistory, setBillingHistory] = useState<Billing[]>([]);
  const [newUser, setNewUser] = useState({ last_name: '', email: '', role: '', password: '', company: '' });
  const [userData, setUserData] = useState({
    id: '',
    last_name: '',
    email: '',
    role: '',
    company: '',
  });
  const [passwords, setPasswords] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });

  const handleInputPasswordChange = (e) => {
    const { id, value } = e.target;
    setPasswords((prev) => ({
      ...prev,
      [id]: value,
    }));
  };
  useEffect(() => {
    // Fetch billing history from the API using axios
    const fetchBillingHistory = async () => {
      try {
        const response = await axiosInstance.get('/api/billingHistory/getAll'); // Replace with your API endpoint
        setBillingHistory(response.data); // Assuming response.data contains the billing data
      } catch (error) {
        console.error('Error fetching billing history:', error);
      }
    };

    fetchBillingHistory();
  }, []);

  const updatePassword = async () => {
    const { currentPassword, newPassword, confirmPassword } = passwords;

    // Validate passwords
    if (newPassword !== confirmPassword) {
      alert('New password and confirm password do not match.');
      return;
    }

    try {
      const response = await axiosInstance.get(
        `http://localhost:8080/api/user/changepassword?new_password=${newPassword}&old_password=${currentPassword}`
      );

      alert(response.data);
    } catch (err) {
      console.log(err);
      alert('Failed to update password');
    }
  };

  // Function to fetch user data from the API
  const fetchUserData = async () => {
    try {
      const response = await axiosInstance.get('http://localhost:8080/api/user/get'); // Replace with your API URL
      const data = response.data;

      setUserData({
        id: data.id,
        last_name: data.last_name || data.first_name,
        role: data.role,
        email: data.email,
        company: data.company || 'Unknown', // Handle missing company data
      });
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  const handleInputChange = (e) => {
    const { id, value } = e.target;
    setUserData((prev) => ({
      ...prev,
      [id]: value,
    }));
  };

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
      setNewUser({ last_name: '', email: '', role: '', password: '', company: '' });
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
          {['Profile', 'Security', 'Users', 'Notifications', 'Billing'].map((tab) => (
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
                      <div className="space-y-2">
                        <Label htmlFor="company">Company</Label>
                        <Input
                          id="company"
                          type="company"
                          value={isEditingUser ? currentUser?.company : newUser.company}
                          onChange={(e) =>
                            isEditingUser
                              ? setCurrentUser({ ...currentUser, company: e.target.value })
                              : setNewUser({ ...newUser, company: e.target.value })
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
          <TabsContent value="profile">
            <Card>
              <div className="p-6">
                <h3 className="text-lg font-medium">Profile Information</h3>
                <div className="mt-4 space-y-4">
                  <div className="grid gap-2">
                    <Label htmlFor="name">Full Name</Label>
                    <Input id="name" value={userData.last_name} onChange={handleInputChange} />
                  </div>
                  <div className="grid gap-2">
                    <Label htmlFor="email">Email</Label>
                    <Input id="email" type="email" value={userData.email} onChange={handleInputChange} />
                  </div>
                  <div className="grid gap-2">
                    <Label htmlFor="company">Company</Label>
                    <Input id="company" value={userData.company} onChange={handleInputChange} />
                  </div>
                  <Button>Save Changes</Button>
                </div>
              </div>
            </Card>
          </TabsContent>

          <TabsContent value="security">
            <Card>
              <div className="p-6">
                <h3 className="text-lg font-medium">Security Settings</h3>

                <div className="mt-6">
                  <div className="flex items-center justify-between mb-6">
                    <div className="space-y-0.5">
                      <div className="font-medium">Two-Factor Authentication</div>
                      <div className="text-sm text-gray-500">Add an extra layer of security to your account</div>
                    </div>
                    <Switch />
                  </div>

                  <div className="space-y-4">
                    <div className="space-y-2">
                      <Label htmlFor="currentPassword">Current Password</Label>
                      <Input
                        id="currentPassword"
                        type="password"
                        className="w-full"
                        value={passwords.currentPassword}
                        onChange={handleInputPasswordChange}
                      />
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="newPassword">New Password</Label>
                      <Input
                        id="newPassword"
                        type="password"
                        className="w-full"
                        value={passwords.newPassword}
                        onChange={handleInputPasswordChange}
                      />
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="confirmPassword">Confirm New Password</Label>
                      <Input
                        id="confirmPassword"
                        type="password"
                        className="w-full"
                        value={passwords.confirmPassword}
                        onChange={handleInputPasswordChange}
                      />
                    </div>
                    <Button variant="outline" className="mt-2" onClick={updatePassword}>
                      Update
                    </Button>
                  </div>
                </div>
              </div>
            </Card>
          </TabsContent>
          <TabsContent value="notifications">
            <Card>
              <div className="p-6">
                <h3 className="text-lg font-medium">Notification Preferences</h3>
                <div className="mt-6 space-y-6">
                  <div className="flex items-center justify-between">
                    <div className="space-y-0.5">
                      <div className="font-medium">Email Notifications</div>
                      <div className="text-sm text-gray-500">Receive email notifications for important updates</div>
                    </div>
                    <Switch />
                  </div>

                  <div className="flex items-center justify-between">
                    <div className="space-y-0.5">
                      <div className="font-medium">SMS Notifications</div>
                      <div className="text-sm text-gray-500">Get SMS alerts for critical events</div>
                    </div>
                    <Switch />
                  </div>

                  <div className="flex items-center justify-between">
                    <div className="space-y-0.5">
                      <div className="font-medium">Browser Notifications</div>
                      <div className="text-sm text-gray-500">Show desktop notifications in your browser</div>
                    </div>
                    <Switch />
                  </div>

                  <div className="flex items-center justify-between">
                    <div className="space-y-0.5">
                      <div className="font-medium">Marketing Communications</div>
                      <div className="text-sm text-gray-500">Receive updates about new features and promotions</div>
                    </div>
                    <Switch />
                  </div>
                </div>
              </div>
            </Card>
          </TabsContent>

          <TabsContent value="billing">
            <div className="space-y-6">
              <Card>
                <div className="p-6">
                  <h3 className="text-lg font-medium mb-4">Payment Methods</h3>
                  <div className="space-y-4">
                    <div className="flex items-center justify-between p-4 border rounded-lg">
                      <div className="space-y-1">
                        <div className="font-medium">VISA •••• 4242</div>
                        <div className="text-sm text-gray-500">Expires 12/2024</div>
                      </div>
                      <div className="text-sm text-gray-500">Default</div>
                    </div>

                    <Button variant="outline" className="mt-4">
                      Add Payment Method
                    </Button>
                  </div>
                </div>
              </Card>

              <Card>
                <CardHeader className="flex flex-row items-center justify-between">
                  <CardTitle>Billing History</CardTitle>
                  <Button variant="outline" size="sm">
                    Download All
                  </Button>
                </CardHeader>
                <CardContent>
                  <div className="rounded-lg border">
                    {/* Iterate through the billingHistory to dynamically create each record */}
                    {billingHistory.map((billing) => (
                      <div key={billing.id} className="flex items-center justify-between p-4 border-b">
                        <div className="space-y-1">
                          <p className="font-medium">
                            {billing.status === 'Paid' ? 'Pro Plan Subscription' : 'Basic Plan Subscription'}
                          </p>
                          <p className="text-sm text-gray-500">{new Date(billing.created_at).toLocaleDateString()}</p>
                        </div>
                        <div className="flex items-center space-x-4">
                          <span className={`text-${billing.status === 'Paid' ? 'green' : 'red'}-600 font-medium`}>
                            {billing.status}
                          </span>
                          <p className="font-medium">{formatCurrency(billing.amount)}</p>
                          <Button variant="ghost" size="sm">
                            View
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </div>
          </TabsContent>
        </div>
      </Tabs>
    </div>
  );
}
