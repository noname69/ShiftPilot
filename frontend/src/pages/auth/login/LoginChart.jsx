import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from "recharts";

const data = [
    { name: "Mon", value: 4 },
    { name: "Tue", value: 5 },
    { name: "Wed", value: 7 },
    { name: "Thu", value: 6 },
    { name: "Mon", value: 9 },
    { name: "Tue", value: 8 },
    { name: "Wed", value: 3 },

];

const getColor = (value) => {
    if (value < 8) return "#000";
    if (value < 9) return "#832626";
    return "#5676bd";
};

export default function LoginChart() {
    return (
        <div className="w-full h-30">
            <ResponsiveContainer width="100%" height={120}>
                <BarChart data={data}>
                    <Bar dataKey="value" radius={[4, 4, 0, 0]}>
                        {data.map((entry, index) => (
                            <Cell key={index} fill={getColor(entry.value)} />
                        ))}
                    </Bar>
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
}